package com.notification.reddert.repositories;

import com.notification.reddert.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}