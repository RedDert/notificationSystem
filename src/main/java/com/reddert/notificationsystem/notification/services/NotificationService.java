package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.notification.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationService(NotificationRepository notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public NotificationDTO createNotification(CreateNotificationDTO createNotificationDTO) {
        Notification notification = new Notification(createNotificationDTO.message(), false);
        Notification savedNotification = notificationRepository.save(notification);

        emailService.sendNotificationEmail("user@example.com", "New Notification",
                "You have a new notification: " + savedNotification.getMessage());

        return NotificationDTO.fromEntity(savedNotification);
    }

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .map(NotificationDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }

    public NotificationDTO markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        return NotificationDTO.fromEntity(notificationRepository.save(notification));
    }

    public NotificationDTO markAsUnread(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(false);
        return NotificationDTO.fromEntity(notificationRepository.save(notification));
    }

    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
}
