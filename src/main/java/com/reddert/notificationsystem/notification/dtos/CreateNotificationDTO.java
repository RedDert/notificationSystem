package com.reddert.notificationsystem.notification.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import static com.reddert.notificationsystem.notification.NotificationsErrorMessages.*;

public record CreateNotificationDTO(


        @NotBlank(message = MESSAGE_CANNOT_BE_BLANK)
        @Size(max = 500, message = MESSAGE_TOO_LONG)
        String message
) {

}
