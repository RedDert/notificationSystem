package com.reddert.notificationsystem.user.repository;

import java.util.UUID;
import com.reddert.notificationsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {
}
