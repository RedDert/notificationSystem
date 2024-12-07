package com.reddert.notificationsystem.team.dtos;

import com.reddert.notificationsystem.team.model.MemberOf;
import com.reddert.notificationsystem.team.model.Team;
import java.util.UUID;

public record UserMembershipDTO(UUID userId, UUID teamId, String teamName, String roleTypeName) {

  public static UserMembershipDTO fromEntity(MemberOf member) {
    Team team = member.getTeam();
    return new UserMembershipDTO(
        member.getUser().getId(),
        team.getId(),
        team.getName(),
        member.getRole().getRoleType().name());
  }
}
