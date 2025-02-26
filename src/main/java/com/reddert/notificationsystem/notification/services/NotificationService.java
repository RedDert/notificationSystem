package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.notification.repositories.NotificationRepository;
import com.reddert.notificationsystem.user.model.User;
import com.reddert.notificationsystem.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final List<NotificationChannel> channels;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               List<NotificationChannel> channels,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.channels = channels;
        this.userRepository = userRepository;
    }

    public NotificationDTO createNotification(UUID userId, CreateNotificationDTO createNotificationDTO) {
        // Validate message
        if (createNotificationDTO.message() == null || createNotificationDTO.message().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be empty.");
        }
        if (createNotificationDTO.message().length() > 500) {
            throw new IllegalArgumentException("Notification message is too long.");
        }

        // Retrieve User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Create and save Notification
        Notification notification = new Notification(createNotificationDTO.message(), false, user);
        Notification savedNotification = notificationRepository.save(notification);

        // Dispatch notification via all channels
        channels.forEach(channel -> {
            try {
                channel.send(savedNotification);
            } catch (Exception e) {
                // Optionally log the exception or handle channel-specific failures
                System.err.println("Failed to send via "
                        + channel.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });

        return NotificationDTO.fromEntity(savedNotification);
    }

    public List<NotificationDTO> getAllNotificationsForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return notificationRepository.findByUser(user)
                .stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(UUID userId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to the user");
        }

        return NotificationDTO.fromEntity(notification);
    }

    public NotificationDTO markAsRead(UUID userId, UUID notificationId) {
        Notification notification = getNotificationByUserId(userId, notificationId);
        notification.setRead(true);
        return NotificationDTO.fromEntity(notificationRepository.save(notification));
    }

    public NotificationDTO markAsUnread(UUID userId, UUID notificationId) {
        Notification notification = getNotificationByUserId(userId, notificationId);
        notification.setRead(false);
        return NotificationDTO.fromEntity(notificationRepository.save(notification));
    }

    public void deleteNotification(UUID userId, UUID notificationId) {
        Notification notification = getNotificationByUserId(userId, notificationId);
        notificationRepository.delete(notification);
    }

    private Notification getNotificationByUserId(UUID userId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to the user");
        }

        return notification;
    }
}
