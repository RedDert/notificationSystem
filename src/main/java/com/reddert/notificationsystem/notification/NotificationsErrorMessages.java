package com.reddert.notificationsystem.notification;

public class NotificationsErrorMessages {

    // Constants for service 
    public static final String NOTIFICATION_MESSAGE_CANNOT_BE_EMPTY = "Notification message cannot be empty.";
    public static final String NOTIFICATION_MESSAGE_TOO_LONG = "Notification message is too long.";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String NOTIFICATION_NOT_FOUND = "Notification not found";
    public static final String NOTIFICATION_DOES_NOT_BELONG_TO_USER = "Notification does not belong to the user";

    //constants for model
    public static final String USER_CANNOT_BE_NULL = "User cannot be null for a notification.";

    //constants for global exception handler

    public static final String AN_ERROR_OCCURRED = "An error occurred: ";
    public static final String ENTITY_NOT_FOUND = "Entity not found: ";
    public static final String WRONG_ARGUMENT = "Wrong argument: ";

    //constants for CreateNotificationDTO
    public static final String MESSAGE_CANNOT_BE_BLANK = "Notification message cannot be blank";
    public static final String MESSAGE_TOO_LONG = "Notification message is too long";


    private NotificationsErrorMessages() {
        // public constructor to prevent instantiation
}
    // This class is a utility class for holding error messages related to notifications.
    // It should not be instantiated, hence the public constructor.
}
