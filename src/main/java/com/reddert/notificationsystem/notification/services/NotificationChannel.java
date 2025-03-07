package com.reddert.notificationsystem.notification.services;

import com.reddert.notificationsystem.notification.model.Notification;

public interface NotificationChannel {
    void send(Notification notification);
}
