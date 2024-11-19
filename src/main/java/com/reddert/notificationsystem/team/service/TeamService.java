package com.reddert.notificationsystem.team.service;

import com.reddert.notificationsystem.team.dtos.CreateTeamDTO;
import com.reddert.notificationsystem.team.dtos.CreateUserMemberShipDTO;
import com.reddert.notificationsystem.team.dtos.TeamDTO;
import com.reddert.notificationsystem.team.dtos.UserMembershipDTO;
import com.reddert.notificationsystem.team.exceptions.TeamExceptionHandler;
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
    User owner =
        userRepository
            .findById(createTeamDTO.ownerId())
            .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

    Team team = new Team(createTeamDTO.name());
    team = teamRepository.save(team);

    Role ownerRole =
        roleRepository
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
    RoleType roleType = createUserMemberShipDTO.roleType();
    MemberOfId memberOfId = new MemberOfId(userId, teamId);
    if (memberOfRepository.existsById(memberOfId)) {
      throw new IllegalArgumentException("User is already a member of the team.");
    }

    // Check if team, user, and role exist
    Team team =
        teamRepository
            .findById(teamId)
            .orElseThrow(() -> new EntityNotFoundException("Team not found with ID: " + teamId));
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    Role role =
        roleRepository
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
    MemberOf requestorMembership =
        memberOfRepository
            .findById(requestorMemberOfId)
            .orElseThrow(
                () -> new IllegalArgumentException("Requestor is not a member of the team."));

    // Verify the target user's membership and role in the team
    MemberOfId targetMemberOfId = new MemberOfId(targetUserId, teamId);
    MemberOf targetMembership =
        memberOfRepository
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
            membership ->
                new UserMembershipDTO(
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
            membership ->
                new UserMembershipDTO(
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
    if (teams.isEmpty()) {
      throw new EntityNotFoundException("No Teams found");
    }
    return teams.stream()
        .map(team -> new TeamDTO(team.getId(), team.getName(), team.getDescription()))
        .collect(Collectors.toList());
  }

  public TeamDTO getTeamById(UUID id) {
    Team team =
        teamRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Team does not exist"));
    return TeamDTO.fromEntity(team);
  }

  /**
   * Changes role of target user when acting as requester user. Owners can change all users Roles
   * Admin can change all users with Roles Guest to Member and viceversa.
   *
   * @param teamId
   * @param requesterId
   * @param targetId
   * @return UserMembershipDTO of user with targetId
   */
  public UserMembershipDTO changeUserRole(
      UUID teamId, UUID requesterId, UUID targetId, String roleTypeString) {
    
    try {
      RoleType targetNewRoleType  = RoleType.valueOf(roleTypeString.toUpperCase());
    } catch Exception e; {
      throw new IllegalArgumentException(e);
  }
/*
    Role targetNewRole =
        roleRepository
            .findByRoleType(targetNewRoleType)
            .orElseThrow(
                () -> new EntityNotFoundException("Role not found, critical Exception"));

    if (isMemberOfTeam(requesterId, teamId) && isMemberOfTeam(targetId, teamId)) {

      RoleType requesterRoleType =
          memberOfRepository.findRole_RoleTypeByTeam_IdAndUser_Id(requesterId, teamId);
      // Only Admins and Owners can change roleType
      if (requesterRoleType == RoleType.ADMIN || requesterRoleType == RoleType.OWNER) {

      } else {
        throw new TeamExceptionHandler("requestee doesn't have permissions to change roles.");
      }

    } else {
      throw new TeamExceptionHandler("Not in same team.");
    } */
  }

  private boolean isMemberOfTeam(UUID userId, UUID teamId) {
    teamRepository
        .findById(teamId)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with ID: " + teamId));

    userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    MemberOfId memberOfId = new MemberOfId(userId, teamId);
    if (memberOfRepository.existsById(memberOfId)) {
      return true;
    }
    return false;
  }

}
