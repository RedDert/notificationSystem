package com.notification.reddert.services;

import com.notification.reddert.dtos.CreateNotificationDTO;
import com.notification.reddert.dtos.NotificationDTO;
import com.notification.reddert.model.Notification;
import com.notification.reddert.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        notificationService = new NotificationService(notificationRepository);
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
        assertEquals("Test notification", result.getFirst().message());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void getNotificationById_shouldReturnNotificationDTOIfFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        Notification notification = new Notification("Test notification", false);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        // Act
        NotificationDTO result = notificationService.getNotificationById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Test notification", result.message());
        verify(notificationRepository, times(1)).findById(id);
    }

  /*  @Test
    void markAsRead_shouldUpdateNotificationAndReturnNotificationDTO() {
        // Arrange
        UUID id = UUID.randomUUID();  // Generate a UUID for the test notification
        Notification notification = new Notification("Test notification", false);
        notification.setId(id);  // Manually set the ID to avoid NullPointerException
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification)); // Mock the repository response

        // Act
        NotificationDTO result = notificationService.markAsRead(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());  // Ensure the ID is correctly passed to the DTO
        assertTrue(result.read());  // Ensure the read status is updated
        verify(notificationRepository, times(1)).save(notification);
    } */


    @Test
    void deleteNotification_shouldRemoveNotification() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        notificationService.deleteNotification(id);

        // Assert
        verify(notificationRepository, times(1)).deleteById(id);
    }
}
