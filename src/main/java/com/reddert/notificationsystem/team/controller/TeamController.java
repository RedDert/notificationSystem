package com.reddert.notificationsystem.team.controller;

import com.reddert.notificationsystem.team.dtos.CreateTeamDTO;
import com.reddert.notificationsystem.team.dtos.CreateUserMemberShipDTO;
import com.reddert.notificationsystem.team.dtos.RoleChangeResponseDTO;
import com.reddert.notificationsystem.team.dtos.TeamDTO;
import com.reddert.notificationsystem.team.dtos.UserMembershipDTO;
import com.reddert.notificationsystem.team.service.TeamService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

  private final TeamService teamService;

  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @GetMapping
  public ResponseEntity<List<TeamDTO>> getAllTeams() {
    return ResponseEntity.ok(teamService.getAllTeams());
  }

  @PostMapping
  public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody CreateTeamDTO createTeamDTO) {
    TeamDTO team = teamService.createTeam(createTeamDTO);
    return new ResponseEntity<>(team, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TeamDTO> getTeamById(@PathVariable UUID id) {
    return ResponseEntity.ok(teamService.getTeamById(id));
  }

  @GetMapping("/{id}/members")
  public ResponseEntity<List<UserMembershipDTO>> getTeamMembersById(@PathVariable UUID id) {
    return ResponseEntity.ok(teamService.getAllMembersInTeam(id));
  }

  @PostMapping("/{id}/members")
  public ResponseEntity<UserMembershipDTO> addUserToTeam(
      @PathVariable UUID id, @RequestBody CreateUserMemberShipDTO createUserMemberShipDTO) {
    return ResponseEntity.ok(teamService.addMemberToTeam(createUserMemberShipDTO));
  }

  @DeleteMapping("/{teamId}/members/{targetId}")
  public ResponseEntity<Void> removeMemberFromTeam(
      @PathVariable UUID teamId, @PathVariable UUID targetId, @RequestParam UUID requestorId) {
    teamService.removeMemberFromTeam(teamId, requestorId, targetId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{teamId}/members/{targetId}")
  public ResponseEntity<RoleChangeResponseDTO> changeMemberRole(
      @PathVariable UUID teamId,
      @PathVariable UUID targetId,
      @RequestParam UUID requestorId,
      @RequestParam String roleType) {

    return ResponseEntity.ok(teamService.changeUserRole(teamId, requestorId, targetId, roleType));
  }
}
