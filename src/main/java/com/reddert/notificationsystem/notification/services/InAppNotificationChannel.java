package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class InAppNotificationChannel implements NotificationChannel {

    @Override
    public void send(Notification notification) {
        // For now, we simply log the notification; replace with in-app dispatch logic as needed.
        System.out.println("In-App Notification for user "
                + notification.getUser().getId() + ": " + notification.getMessage());
    }
}
