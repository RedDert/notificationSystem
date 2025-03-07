package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

class EmailNotificationChannelTest {

    private JavaMailSender mailSender;
    private EmailNotificationChannel emailChannel;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailChannel = new EmailNotificationChannel(mailSender);
    }

    @Test
    void send_shouldSendEmailWithCorrectDetails() {
        // Arrange: create a dummy notification with a user
        User user = new User("Zlatan Ibrahimovic", "Zlatan.Ibra@gmail.com");
        user.setId(UUID.randomUUID());
        Notification notification = new Notification("Tjaba! 500k i veckan?", false, user);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailChannel.send(notification);

        // Assert: verify that the mail sender sent the message with expected values.
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertNotNull(sentMessage.getTo(), "Recipient email address should not be null");
        assertEquals("Zlatan.Ibra@gmail.com", sentMessage.getTo()[0]);
        assertEquals("New Notification", sentMessage.getSubject());
        assertEquals("You have a new notification: Tjaba! 500k i veckan?", sentMessage.getText());
        assertEquals("big.boss@gmail.com", sentMessage.getFrom());
    }
}
