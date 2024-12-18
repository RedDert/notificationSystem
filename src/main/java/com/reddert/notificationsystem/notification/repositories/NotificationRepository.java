package com.reddert.notificationsystem.notification.repositories;

import com.reddert.notificationsystem.notification.model.Notification;
import com.reddert.notificationsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUser(User user);
}
