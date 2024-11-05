package com.reddert.notificationsystem.user.dtos;

import com.reddert.notificationsystem.user.model.User;

import java.util.UUID;

public record UserDTO(UUID id, String name, String email) {

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

}