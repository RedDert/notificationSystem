package com.reddert.notificationsystem.team.dtos;
 
import java.util.UUID;

import com.reddert.notificationsystem.team.model.RoleType;

public record CreateUserMemberShipDTO(UUID userId, UUID teamId, RoleType roleType) {}
