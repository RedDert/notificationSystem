package com.notification.reddert.dtos;

import com.notification.reddert.model.Notification;
import java.util.UUID;

public record NotificationDTO(UUID id, String message, boolean read) {
    public static NotificationDTO fromEntity(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.isRead()
        );
    }
}