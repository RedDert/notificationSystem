package com.reddert.notificationsystem.notification.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNotificationDTO(
        @NotBlank(message = "Notification message cannot be blank")
        @Size(max = 500, message = "Notification message is too long")
        String message
) {
}
