package com.reddert.notificationsystem.notification.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNotificationDTO(


        @NotBlank(message = MESSAGE_CANNOT_BE_BLANK)
        @Size(max = 500, message = MESSAGE_TOO_LONG)
        String message
) {

        private static final String MESSAGE_CANNOT_BE_BLANK = "Notification message cannot be blank";
        private static final String MESSAGE_TOO_LONG = "Notification message is too long";
}
