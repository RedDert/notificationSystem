package com.reddert.notificationsystem.user.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        @Pattern(regexp = "[A-Za-z\\s]+", message = "Name can only contain alphabetic characters and spaces")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {}
