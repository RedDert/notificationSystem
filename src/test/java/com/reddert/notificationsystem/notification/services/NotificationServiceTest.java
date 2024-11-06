package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.notification.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNotification_shouldCreateAndReturnNotificationDTO() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("Test notification");
        Notification notification = new Notification("Test notification", false);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        NotificationDTO result = notificationService.createNotification(createNotificationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test notification", result.message());
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendNotificationEmail(
                eq("user@example.com"),
                eq("New Notification"),
                eq("You have a new notification: " + notification.getMessage())
        );
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotificationDTOs() {
        // Arrange
        Notification notification = new Notification("Test notification", false);
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        // Act
        List<NotificationDTO> result = notificationService.getAllNotifications();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test notification", result.get(0).message());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void getNotificationById_shouldReturnNotificationDTOIfFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        Notification notification = new Notification("Test notification", false);
        notification.setId(id);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        // Act
        NotificationDTO result = notificationService.getNotificationById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Test notification", result.message());
        assertEquals(id, result.id());
        verify(notificationRepository, times(1)).findById(id);
    }

    @Test
    void markAsRead_shouldUpdateNotificationAndReturnNotificationDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        Notification notification = new Notification("Test notification", false);
        notification.setId(id);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        // Act
        NotificationDTO result = notificationService.markAsRead(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertTrue(result.read());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void deleteNotification_shouldRemoveNotification() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        notificationService.deleteNotification(id);

        // Assert
        verify(notificationRepository, times(1)).deleteById(id);
    }

    @Test
    void createNotification_withEmptyMessage_shouldThrowException() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(createNotificationDTO));
    }

    @Test
    void createNotification_withTooLongMessage_shouldThrowException() {
        // Arrange
        String longMessage = "a".repeat(501);
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO(longMessage);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(createNotificationDTO));
    }

    @Test
    void createNotification_withInvalidEmail_shouldThrowException() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("Test message");

        // Mock the repository to save and return a valid notification before throwing exception on email send
        Notification notification = new Notification("Test message", false);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Mock the email service to throw an exception on invalid email
        doThrow(new IllegalArgumentException("Invalid email address."))
                .when(emailService)
                .sendNotificationEmail(anyString(), anyString(), anyString());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(createNotificationDTO));
    }

}
