package com.reddert.notificationsystem.notification.repositories;

import com.reddert.notificationsystem.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}