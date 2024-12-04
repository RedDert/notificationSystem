package com.reddert.notificationsystem.notification.model;

import com.reddert.notificationsystem.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String message;

    private boolean read = false;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private final User user; // Make the field final for security

    public Notification() {
        // Default constructor for JPA
        this.user = null;
    }

    public Notification(String message, boolean read, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null for a notification.");
        }
        this.message = message;
        this.read = read;
        this.timestamp = LocalDateTime.now();
        this.user = user;
    }

    // Getters and setters but only getters for 'user' field

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }
}
