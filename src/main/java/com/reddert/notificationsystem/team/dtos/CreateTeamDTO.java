package com.reddert.notificationsystem.team.dtos;

import java.util.UUID;

public record CreateTeamDTO(String name, String description, UUID ownerId) {}
