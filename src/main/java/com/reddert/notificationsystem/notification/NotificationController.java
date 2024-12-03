package com.reddert.notificationsystem.notification;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @PathVariable UUID userId,
            @RequestBody CreateNotificationDTO createNotificationDTO
    ) {
        return ResponseEntity.ok(
                notificationService.createNotification(userId, createNotificationDTO)
        );
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getAllNotificationsForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(
            @PathVariable UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(notificationService.getNotificationById(userId, id));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(
            @PathVariable UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(notificationService.markAsRead(userId, id));
    }

    @PutMapping("/{id}/unread")
    public ResponseEntity<NotificationDTO> markAsUnread(
            @PathVariable UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(notificationService.markAsUnread(userId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable UUID userId,
            @PathVariable UUID id
    ) {
        notificationService.deleteNotification(userId, id);
        return ResponseEntity.noContent().build();
    }
}
