package com.reddert.notificationsystem.team.service;

import com.reddert.notificationsystem.team.dtos.CreateTeamDTO;
import com.reddert.notificationsystem.team.dtos.CreateUserMemberShipDTO;
import com.reddert.notificationsystem.team.dtos.RoleChangeResponseDTO;
import com.reddert.notificationsystem.team.dtos.TeamDTO;
import com.reddert.notificationsystem.team.dtos.UserMembershipDTO;
import com.reddert.notificationsystem.team.model.MemberOf;
import com.reddert.notificationsystem.team.model.MemberOfId;
import com.reddert.notificationsystem.team.model.Role;
import com.reddert.notificationsystem.team.model.RoleType;
import com.reddert.notificationsystem.team.model.Team;
import com.reddert.notificationsystem.team.repository.MemberOfRepository;
import com.reddert.notificationsystem.team.repository.RoleRepository;
import com.reddert.notificationsystem.team.repository.TeamRepository;
import com.reddert.notificationsystem.user.model.User;
import com.reddert.notificationsystem.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

  private final TeamRepository teamRepository;
  private final UserRepository userRepository;
  private final MemberOfRepository memberOfRepository;
  private final RoleRepository roleRepository;

  public TeamService(
      TeamRepository teamRepository,
      UserRepository userRepository,
      MemberOfRepository memberOfRepository,
      RoleRepository roleRepository) {
    this.teamRepository = teamRepository;
    this.userRepository = userRepository;
    this.memberOfRepository = memberOfRepository;
    this.roleRepository = roleRepository;
  }

  @Transactional
  public TeamDTO createTeam(CreateTeamDTO createTeamDTO) {
    User owner = userRepository
        .findById(createTeamDTO.ownerId())
        .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

    Team team = new Team(createTeamDTO.name());
    team = teamRepository.save(team);

    Role ownerRole = roleRepository
        .findByRoleType(RoleType.OWNER)
        .orElseThrow(
            () -> new EntityNotFoundException("OWNER Role not found, critical Exception"));
    // Make owner member of team
    MemberOf memberOf = new MemberOf(owner, team, ownerRole);
    memberOf = memberOfRepository.save(memberOf);
    return TeamDTO.fromEntity(team);
  }

  @Transactional
  public UserMembershipDTO addMemberToTeam(CreateUserMemberShipDTO createUserMemberShipDTO) {
    // Check if the user is already a member of the team
    UUID userId = createUserMemberShipDTO.userId();
    UUID teamId = createUserMemberShipDTO.teamId();
    RoleType roleType = RoleType.MEMBER;
    MemberOfId memberOfId = new MemberOfId(userId, teamId);
    if (memberOfRepository.existsById(memberOfId)) {
      throw new IllegalArgumentException("User is already a member of the team.");
    }

    // Check if team, user, and role exist
    Team team = teamRepository
        .findById(teamId)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with ID: " + teamId));
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    Role role = roleRepository
        .findByRoleType(roleType)
        .orElseThrow(
            () -> new EntityNotFoundException("Role not found for role type: " + roleType));

    // Create and save an entry in member_of table
    MemberOf memberOf = new MemberOf(user, team, role);
    memberOfRepository.save(memberOf);

    // Return UserMembershipDTO with team and role information
    return new UserMembershipDTO(
        user.getId(),
        team.getId(),
        team.getName(),
        role.getRoleType().name() // Convert role type to string
    );
  }

  @Transactional
  public void removeMemberFromTeam(UUID teamId, UUID requestorId, UUID targetUserId) {
    // Verify the team exists
    teamRepository
        .findById(teamId)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with ID: " + teamId));

    // Verify the requestor's membership and role in the team
    MemberOfId requestorMemberOfId = new MemberOfId(targetUserId, teamId);
    MemberOf requestorMembership = memberOfRepository
        .findById(requestorMemberOfId)
        .orElseThrow(
            () -> new IllegalArgumentException("Requestor is not a member of the team."));

    // Verify the target user's membership and role in the team
    MemberOfId targetMemberOfId = new MemberOfId(targetUserId, teamId);
    MemberOf targetMembership = memberOfRepository
        .findById(targetMemberOfId)
        .orElseThrow(() -> new IllegalArgumentException("Target is not a member of the team."));

    RoleType requestorRole = requestorMembership.getRole().getRoleType();

    RoleType targetRole = targetMembership.getRole().getRoleType();

    // Check permissions based on roles
    if (requestorRole == RoleType.OWNER
        || (requestorRole == RoleType.ADMIN
            && targetRole != RoleType.ADMIN
            && targetRole != RoleType.OWNER)) {
      // Remove the target user from the team
      memberOfRepository.delete(targetMembership);
    } else {
      throw new IllegalArgumentException("Insufficient permissions to remove this member.");
    }
  }

  @Transactional(readOnly = true)
  public List<UserMembershipDTO> getUserMemberships(UUID userId) {
    userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    return memberOfRepository.findAllByUserId(userId).stream()
        .map(
            membership -> new UserMembershipDTO(
                membership.getUser().getId(),
                membership.getTeam().getId(),
                membership.getTeam().getName(),
                membership.getRole().getRoleType().name() // Get the role type as a string
            ))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UserMembershipDTO> getAllMembersInTeam(UUID teamId) {
    // Check if the team exists
    teamRepository
        .findById(teamId)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with ID: " + teamId));

    return memberOfRepository.findAllByTeamId(teamId).stream()
        .map(
            membership -> new UserMembershipDTO(
                membership.getUser().getId(),
                membership.getTeam().getId(),
                membership.getTeam().getName(),
                membership.getRole().getRoleType().name() // Get the role type as a string
            ))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<TeamDTO> getAllTeams() {

    List<Team> teams = teamRepository.findAll();
    if (teams.size() == 0) {
      throw new EntityNotFoundException("No Teams found");
    }
    return teams.stream()
        .map(team -> new TeamDTO(team.getId(), team.getName(), team.getDescription()))
        .collect(Collectors.toList());
  }

  public TeamDTO getTeamById(UUID id) {
    Team team = teamRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " not found"));
    return TeamDTO.fromEntity(team);
  }

  /**
   * Changes role of target user when acting as requester user. Owners can change
   * all users Roles
   * Admin can change all users with Roles Guest to Member and viceversa.
   *
   * @param teamId
   * @param requesterId
   * @param targetId
   * @param roleTypeString
   * @return UserMembershipDTO of user with targetId
   */
  @Transactional
  public RoleChangeResponseDTO changeUserRole(
      UUID teamId, UUID requesterId, UUID targetId, String roleTypeString) {

    // check all passed data is valid.
    RoleType targetNewRoleType;
    try {
      targetNewRoleType = RoleType.valueOf(roleTypeString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("invalid role type: " + roleTypeString);
    }

    MemberOfId targetMemberOfId = new MemberOfId(targetId, teamId);
    MemberOf targetMember = memberOfRepository
        .findById(targetMemberOfId)
        .orElseThrow(() -> new EntityNotFoundException("target user does not exist"));

    MemberOfId requesterMemberOfId = new MemberOfId(requesterId, teamId);
    MemberOf requestMember = memberOfRepository
        .findById(requesterMemberOfId)
        .orElseThrow(() -> new EntityNotFoundException("requester user does not exist"));

    Role targetNewRole = roleRepository
        .findByRoleType(targetNewRoleType)
        .orElseThrow(() -> new EntityNotFoundException("Role not found, critical Exception"));

    RoleType targetCurrentRoleType = memberOfRepository.findRole_RoleTypeById(targetMemberOfId);

    RoleType requesterRoleType = memberOfRepository.findRole_RoleTypeById(requesterMemberOfId);

    // Case Owner transfer.
    if (requesterRoleType == RoleType.OWNER && targetNewRoleType == RoleType.OWNER) {
      // make owner admin.
      requestMember.setRole(
          roleRepository
              .findByRoleType(RoleType.ADMIN)
              .orElseThrow(
                  () -> new EntityNotFoundException("RoleType Admin not found, Critical Error.")));
      memberOfRepository.save(requestMember);

    // Case Admin does illegal promotion.
    } else if (requesterRoleType == RoleType.ADMIN
        && (targetCurrentRoleType == RoleType.OWNER
            || targetCurrentRoleType == RoleType.ADMIN
            || targetNewRoleType == RoleType.OWNER
            || targetNewRoleType == RoleType.ADMIN)) {

      throw new IllegalArgumentException(
          String.format(
              "user %d doesn't have permissions to change user %d from %s to %s",
              requesterId,
              targetId,
              targetCurrentRoleType.toString(),
              targetNewRoleType.toString()));

    // Case non owner or admin tries to change role.
    } else if (requesterRoleType != RoleType.OWNER && requesterRoleType != RoleType.ADMIN) {
      throw new IllegalArgumentException("requestee doesn't have permissions to change roles.");
    }

    targetMember.setRole(targetNewRole);
    memberOfRepository.save(targetMember);
    return new RoleChangeResponseDTO(UserMembershipDTO.fromEntity(targetMember),
        UserMembershipDTO.fromEntity(requestMember));
  }

  @Transactional
  public UserMembershipDTO deleteMemberFromTeam(UUID teamId, UUID requesterId, UUID targetId)
      throws AccessDeniedException {

    MemberOfId requesterMemberOfId = new MemberOfId(requesterId, teamId);
    memberOfRepository
        .findById(requesterMemberOfId)
        .orElseThrow(() -> new EntityNotFoundException("requester user does not exist"));

    RoleType requesterRoleType = memberOfRepository.findRole_RoleTypeById(requesterMemberOfId);

    MemberOfId targetMemberOfId = new MemberOfId(targetId, teamId);
    MemberOf targetMember = memberOfRepository
        .findById(targetMemberOfId)
        .orElseThrow(() -> new EntityNotFoundException("target user does not exist"));

    RoleType targetRoleType = memberOfRepository.findRole_RoleTypeById(targetMemberOfId);

    UserMembershipDTO deletedMemberDTO;

    // Case admin tries to delete other admin or owner
    validateRequesterPermissions(teamId, requesterId, requesterRoleType, targetRoleType);

    // Case self deletion
    if (requesterId.equals(targetId)) {
      handleOwnerSelfDeletion(teamId, targetMember);
    }

    deletedMemberDTO = UserMembershipDTO.fromEntity(targetMember);
    memberOfRepository.delete(targetMember);
    return deletedMemberDTO;
  }

  // helper functions
  private void validateRequesterPermissions(
      UUID teamId, UUID requesterId, RoleType requesterRoleType, RoleType targetRoleType) {
    if (requesterRoleType != RoleType.OWNER && requesterRoleType != RoleType.ADMIN) {
      throw new IllegalAccessError(
          String.format("User %s not permitted to delete users from team %s", requesterId, teamId));
    }
    if (requesterRoleType == RoleType.ADMIN
        && (targetRoleType == RoleType.ADMIN || targetRoleType == RoleType.OWNER)) {
      throw new IllegalAccessError(
          String.format(
              "Admin user %s is not allowed to delete other admins or the owner in team %s",
              requesterId, teamId));
    }
  }

  private void handleOwnerSelfDeletion(UUID teamId, MemberOf targetMember) {
    memberOfRepository.delete(targetMember);
    if (!memberOfRepository.existsByTeamId(teamId)) {
      teamRepository.deleteById(teamId);
    } else {
      promoteNewOwnerIfPossible(teamId);
    }
  }

  private void promoteNewOwnerIfPossible(UUID teamId) {
    List<MemberOf> admins = memberOfRepository.findAllByTeam_IdAndRole_RoleType(teamId, RoleType.ADMIN);
    if (!admins.isEmpty()) {
      MemberOf newOwner = admins.get(0);
      newOwner.getRole().setRoleType(RoleType.OWNER);
      memberOfRepository.save(newOwner);
    } else {
      // promote a member if no admins are available
      List<MemberOf> members = memberOfRepository.findAllByTeam_IdAndRole_RoleType(teamId, RoleType.MEMBER);
      if (!members.isEmpty()) {
        MemberOf newOwner = members.get(0);
        newOwner.getRole().setRoleType(RoleType.OWNER);
        memberOfRepository.save(newOwner);
      } else {
        teamRepository.deleteById(teamId);
      }
    }
  }
}
