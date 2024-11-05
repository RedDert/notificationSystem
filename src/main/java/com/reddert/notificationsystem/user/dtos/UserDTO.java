package com.reddert.notificationsystem.user.dtos;

import java.util.UUID;

public record UserDTO(UUID id, String name, String email) {}