package com.notification.reddert.services;

import com.notification.reddert.dtos.CreateNotificationDTO;
import com.notification.reddert.dtos.NotificationDTO;
import com.notification.reddert.model.Notification;
import com.notification.reddert.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDTO createNotification(CreateNotificationDTO createNotificationDTO) {
        Notification notification = new Notification(createNotificationDTO.message(), false);
        return NotificationDTO.fromEntity(notificationRepository.save(notification));
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