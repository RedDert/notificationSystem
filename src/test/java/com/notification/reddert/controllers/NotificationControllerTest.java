package com.notification.reddert.controllers;

import com.notification.reddert.model.Notification;
import com.notification.reddert.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        // Arrange
        String requestBody = "{\"message\":\"Test notification\"}";

        // Act & Assert
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void getAllNotifications_shouldReturnNotificationList() throws Exception {
        // Clear the repository to ensure no previous data affects the test
        notificationRepository.deleteAll();

        // Arrange
        notificationRepository.save(new Notification("Test notification", false));

        // Act & Assert
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    void getNotificationById_shouldReturnNotification() throws Exception {
        // Arrange
        Notification notification = notificationRepository.save(new Notification("Test notification", false));

        // Act & Assert
        mockMvc.perform(get("/notifications/{id}", notification.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test notification"));
    }

    @Test
    void markAsRead_shouldUpdateNotificationReadStatus() throws Exception {
        // Arrange
        Notification notification = notificationRepository.save(new Notification("Test notification", false));

        // Act & Assert
        mockMvc.perform(put("/notifications/{id}/read", notification.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read").value(true));
    }

    @Test
    void deleteNotification_shouldRemoveNotification() throws Exception {
        // Arrange
        Notification notification = notificationRepository.save(new Notification("Test notification", false));

        // Act & Assert
        mockMvc.perform(delete("/notifications/{id}", notification.getId()))
                .andExpect(status().isNoContent());
    }
}
