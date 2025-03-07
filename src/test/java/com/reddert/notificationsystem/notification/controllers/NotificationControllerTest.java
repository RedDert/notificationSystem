package com.reddert.notificationsystem.notification.controllers;

import com.reddert.notificationsystem.notification.NotificationController;
import com.reddert.notificationsystem.notification.dtos.CreateNotificationDTO;
import com.reddert.notificationsystem.notification.dtos.NotificationDTO;
import com.reddert.notificationsystem.notification.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        NotificationDTO mockNotification = new NotificationDTO(UUID.randomUUID(), "Test notification", false);

        when(notificationService.createNotification(eq(userId), any(CreateNotificationDTO.class)))
                .thenReturn(mockNotification);
        when(notificationService.getAllNotificationsForUser(eq(userId)))
                .thenReturn(List.of(mockNotification));
        when(notificationService.getNotificationById(eq(userId), any(UUID.class)))
                .thenReturn(mockNotification);
        when(notificationService.markAsRead(eq(userId), any(UUID.class)))
                .thenReturn(mockNotification);
        when(notificationService.markAsUnread(eq(userId), any(UUID.class)))
                .thenReturn(mockNotification);
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        String requestBody = "{\"message\":\"Test notification\"}";

        mockMvc.perform(post("/users/{userId}/notifications", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void getAllNotifications_shouldReturnNotificationList() throws Exception {
        mockMvc.perform(get("/users/{userId}/notifications", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    void getNotificationById_shouldReturnNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(get("/users/{userId}/notifications/{id}", userId, notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void markAsRead_shouldReturnUpdatedNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(put("/users/{userId}/notifications/{id}/read", userId, notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void markAsUnread_shouldReturnUpdatedNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(put("/users/{userId}/notifications/{id}/unread", userId, notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    // --- Validation Tests ---

    @Test
    void createNotification_withBlankMessage_shouldReturnBadRequest() throws Exception {
        String requestBody = "{\"message\":\"\"}";

        mockMvc.perform(post("/users/{userId}/notifications", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotification_withTooLongMessage_shouldReturnBadRequest() throws Exception {
        String longMessage = "a".repeat(501);
        String requestBody = "{\"message\":\"" + longMessage + "\"}";

        mockMvc.perform(post("/users/{userId}/notifications", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
