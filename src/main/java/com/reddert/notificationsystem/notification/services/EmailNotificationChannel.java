package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;
    private static final String FROM_EMAIL = "big.boss@gmail.com";

    @Autowired
    public EmailNotificationChannel(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(Notification notification) {
        String recipientEmail = notification.getUser().getEmail();
        // Optionally add email validation logic here
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(recipientEmail);
        message.setSubject("New Notification");
        message.setText("You have a new notification: " + notification.getMessage());
        mailSender.send(message);
    }
}
