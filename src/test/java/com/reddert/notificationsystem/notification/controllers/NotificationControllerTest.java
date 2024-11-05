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

    @BeforeEach
    void setUp() {
        // Create a mock NotificationDTO
        NotificationDTO mockNotification = new NotificationDTO(UUID.randomUUID(), "Test notification", false);

        // Mock NotificationService methods
        when(notificationService.createNotification(any(CreateNotificationDTO.class))).thenReturn(mockNotification);
        when(notificationService.getAllNotifications()).thenReturn(List.of(mockNotification));
        when(notificationService.getNotificationById(any(UUID.class))).thenReturn(mockNotification);
        when(notificationService.markAsRead(any(UUID.class))).thenReturn(mockNotification);
        when(notificationService.markAsUnread(any(UUID.class))).thenReturn(mockNotification);
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        String requestBody = "{\"message\":\"Test notification\"}";

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void getAllNotifications_shouldReturnNotificationList() throws Exception {
        mockMvc.perform(get("/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    void getNotificationById_shouldReturnNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(get("/notifications/{id}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void markAsRead_shouldReturnUpdatedNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(put("/notifications/{id}/read", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void markAsUnread_shouldReturnUpdatedNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(put("/notifications/{id}/unread", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }
}
