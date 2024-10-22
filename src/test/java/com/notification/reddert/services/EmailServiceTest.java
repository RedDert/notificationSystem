package com.notification.reddert.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {

        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
    }

    @Test
    void sendNotificationEmail_shouldSendEmailWithCorrectDetails() {
        // Arrange
        String to = "Zlatan.Ibra@gmail.com";
        String subject = "Nytt kontraktsförslag";
        String text = "Tjaba! 500k i veckan?";
        String expectedFrom = "big.boss@gmail.com";

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.sendNotificationEmail(to, subject, text);

        // Assert
        // Verify send() was called once
        verify(mailSender, times(1)).send(messageCaptor.capture());
        // Get the captured argument
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertNotNull(sentMessage.getTo(), "Recipient email address (to) should not be null");
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
        assertEquals(expectedFrom, sentMessage.getFrom());
    }
}