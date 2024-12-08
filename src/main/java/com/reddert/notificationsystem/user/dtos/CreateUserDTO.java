package com.reddert.notificationsystem.user.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email String email
) {}
