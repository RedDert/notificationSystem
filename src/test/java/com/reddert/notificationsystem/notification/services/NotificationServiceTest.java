package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.notification.repositories.NotificationRepository;
import com.reddert.notificationsystem.user.model.User;
import com.reddert.notificationsystem.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationChannel notificationChannel;

    @Mock
    private UserRepository userRepository;

    // We'll instantiate NotificationService manually to supply the list of channels.
    private NotificationService notificationService;

    private User mockUser;
    private UUID userId;
    private UUID notificationId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        notificationId = UUID.randomUUID();
        mockUser = new User("Lionel Messi", "lionel.messi@gmail.com");
        mockUser.setId(userId);

        // Provide the channels list explicitly using the mocked notificationChannel.
        notificationService = new NotificationService(notificationRepository, List.of(notificationChannel), userRepository);
    }

    @Test
    void createNotification_shouldCreateAndReturnNotificationDTO() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("Test notification");
        Notification notification = new Notification("Test notification", false, mockUser);
        notification.setId(notificationId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        NotificationDTO result = notificationService.createNotification(userId, createNotificationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test notification", result.message());
        verify(userRepository, times(1)).findById(userId);
        verify(notificationRepository, times(1)).save(any(Notification.class));

        // Verify that the channel's send() method was called
        verify(notificationChannel, times(1)).send(notification);
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotificationDTOs() {
        // Arrange
        Notification notification = new Notification("Test notification", false, mockUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(notificationRepository.findByUser(mockUser)).thenReturn(List.of(notification));

        // Act
        List<NotificationDTO> result = notificationService.getAllNotificationsForUser(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test notification", result.get(0).message());
        verify(notificationRepository, times(1)).findByUser(mockUser);
    }

    @Test
    void getNotificationById_shouldReturnNotificationDTOIfFound() {
        // Arrange
        Notification notification = new Notification("Test notification", false, mockUser);
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // Act
        NotificationDTO result = notificationService.getNotificationById(userId, notificationId);

        // Assert
        assertNotNull(result);
        assertEquals("Test notification", result.message());
        assertEquals(notificationId, result.id());
        verify(notificationRepository, times(1)).findById(notificationId);
    }

    @Test
    void markAsRead_shouldUpdateNotificationAndReturnNotificationDTO() {
        // Arrange
        Notification notification = new Notification("Test notification", false, mockUser);
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        // Act
        NotificationDTO result = notificationService.markAsRead(userId, notificationId);

        // Assert
        assertNotNull(result);
        assertEquals(notificationId, result.id());
        assertTrue(result.read());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void markAsUnread_shouldUpdateNotificationAndReturnNotificationDTO() {
        // Arrange
        Notification notification = new Notification("Test notification", true, mockUser);
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        // Act
        NotificationDTO result = notificationService.markAsUnread(userId, notificationId);

        // Assert
        assertNotNull(result);
        assertEquals(notificationId, result.id());
        assertFalse(result.read());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void deleteNotification_shouldRemoveNotification() {
        // Arrange
        Notification notification = new Notification("Test notification", false, mockUser);
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // Act
        notificationService.deleteNotification(userId, notificationId);

        // Assert
        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void createNotification_withEmptyMessage_shouldThrowException() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                notificationService.createNotification(userId, createNotificationDTO)
        );
    }

    @Test
    void createNotification_withTooLongMessage_shouldThrowException() {
        // Arrange
        String longMessage = "a".repeat(501);
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO(longMessage);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                notificationService.createNotification(userId, createNotificationDTO)
        );
    }

    @Test
    void createNotification_withNonexistentUser_shouldThrowException() {
        // Arrange
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO("Test message");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                notificationService.createNotification(userId, createNotificationDTO)
        );
    }
}
