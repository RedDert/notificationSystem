package com.reddert.notificationsystem.notification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotificationEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        // needed to set it to an actual email address *dabs* yes dabbing is coming back in 2025 mark my words
        message.setFrom("big.boss@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
