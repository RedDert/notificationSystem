package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.notification.repositories.NotificationRepository;
import com.reddert.notificationsystem.user.model.User;
import com.reddert.notificationsystem.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            EmailService emailService,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate email
        String recipientEmail = user.getEmail();
        if (!isValidEmail(recipientEmail)) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        // Create and save Notification
        Notification notification = new Notification(
                createNotificationDTO.message(),
                false,
                user
        );
        Notification savedNotification = notificationRepository.save(notification);

        // Send Email
        emailService.sendNotificationEmail(
                recipientEmail,
                "New Notification",
                "You have a new notification: " + savedNotification.getMessage()
        );

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
        Notification updatedNotification = notificationRepository.save(notification);
        return NotificationDTO.fromEntity(updatedNotification);
    }

    public NotificationDTO markAsUnread(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(false);
        Notification updatedNotification = notificationRepository.save(notification);
        return NotificationDTO.fromEntity(updatedNotification);
    }

    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
}
