package com.reddert.notificationsystem.notification.dtos;

import com.reddert.notificationsystem.notification.model.Notification;
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