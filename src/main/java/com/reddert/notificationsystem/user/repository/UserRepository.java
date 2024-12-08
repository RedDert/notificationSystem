package com.reddert.notificationsystem.user.repository;

import java.util.Optional;
import java.util.UUID;
import com.reddert.notificationsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}