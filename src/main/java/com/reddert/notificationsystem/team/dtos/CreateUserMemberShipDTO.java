package com.reddert.notificationsystem.team.dtos;
 
import java.util.UUID;

public record CreateUserMemberShipDTO(UUID userId, UUID teamId, String teamName, String roleTypeString) {}
