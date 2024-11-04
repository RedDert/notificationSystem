package com.reddert.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EmailServiceIntegrationTest {

    @Autowired
    private JavaMailSender mailSender;
    //this test is disabled to get around the build issues on the remote, you should only enable it if you have the .env file

  /*  @Test
    void testEmailConfiguration() {
        // Use a try-catch block to attempt sending a simple test email
        assertDoesNotThrow(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test-recipient@example.com"); // Use a dummy email for testing
            message.setSubject("Test Email Configuration");
            message.setText("This is a test email to verify the configuration.");

            try {
                mailSender.send(message);
            } catch (MailException e) {
                // Handle potential errors without failing the test, in case the SMTP server is unreachable
                throw new RuntimeException("Email configuration test failed.", e);
            }
        });
    } */
}
