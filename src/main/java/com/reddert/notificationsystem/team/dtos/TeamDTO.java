package com.reddert.notificationsystem.team.dtos;

import java.util.UUID;

import com.reddert.notificationsystem.team.model.Team;

public record TeamDTO(UUID id, String name, String description) {
  public static TeamDTO fromEntity(Team team) {
    return new TeamDTO(
        team.getId(),
        team.getName(),
        team.getDescription());
  }
}
