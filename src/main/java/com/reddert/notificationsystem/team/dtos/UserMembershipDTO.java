package com.reddert.notificationsystem.team.dtos;

import java.util.UUID;

public class UserMembershipDTO {
  private UUID userId;
  private UUID teamId;
  private String teamName;
  private String role;

  public UserMembershipDTO(UUID userId, UUID teamId, String teamName, String role) {
    this.userId = userId;
    this.teamId = teamId;
    this.teamName = teamName;
    this.role = role;
  }

  public UUID getUserId() {
    return userId;
  }
  public UUID getTeamId() {
    return teamId;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getRole() {
    return role;
  }
}
