package com.reddert.notificationsystem.user;

public final class UserErrorMessages {

    public static final String EMAIL_ALREADY_EXISTS = "Email is already associated with an existing user.";
    public static final String EMAIL_EXISTS_FOR_ANOTHER_USER = "Email is already associated with another user.";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with ID: ";

    private UserErrorMessages() {
        // Prevent instantiation
    }
}
