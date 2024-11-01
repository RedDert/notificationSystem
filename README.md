# Notification Service Project

## Overview
This project is a backend notification service built with Java and Spring Boot, designed to manage user notifications and send email alerts. The main objective is to demonstrate fundamental backend development skills, such as RESTful API design, email integration, and comprehensive testing.

## Features
- **REST API for Notifications**: Users can create, retrieve, mark as read/unread, and delete notifications.
- **Email Notifications**: Sends email notifications upon creating a new notification, showcasing email service integration with external SMTP (Gmail) support.
- **Testing Suite**:
    - **Unit Testing**: Service and controller layers are thoroughly tested to validate business logic.
    - **Integration Testing**: End-to-end email configuration tests ensure correct setup and functionality.

## Technologies
- **Java 21**
- **Spring Boot 3.3.4** (with Spring Data JPA, Spring Web, and Spring Boot Starter Mail)
- **H2 In-Memory Database**: Used for testing purposes.
- **JUnit and Mockito**: For unit and integration testing.
- **GitHub Actions**: Linting and continuous integration workflows.

## Installation
1. **Clone the Repository**:
   ```bash
   git clone 
   cd into the project dir
   ```

2. **Environment Setup**:
    - Create an `.env` file in the root directory with your email configuration:
      ```plaintext
      EMAIL_USERNAME=your-email@example.com
      EMAIL_PASSWORD='your-app-password'
      ```
    - The project reads these credentials to authenticate with Gmail’s SMTP server for sending emails.


3. **Run the Application**:
    - Use the following command to start the application:
      ```bash
      mvnw spring-boot:run
      ```

## Endpoints
- **POST /notifications**: Creates a new notification and sends an email alert.
- **GET /notifications**: Retrieves all notifications.
- **GET /notifications/{id}**: Retrieves a specific notification by ID.
- **PUT /notifications/{id}/read**: Marks a notification as read.
- **PUT /notifications/{id}/unread**: Marks a notification as unread.
- **DELETE /notifications/{id}**: Deletes a notification by ID.

## Testing
1. **Run Tests**:
    - To run all tests, use:
      ```bash
      mvnw test
      ```

2. **Test Coverage**:
    - The testing suite includes:
        - **Unit Tests** for services and controllers.
        - **Integration Test** for email configuration, ensuring valid authentication and setup.

## Additional Notes
- **GitHub Actions**:
    - The project includes GitHub Actions workflows for continuous integration and linting, which run automatically on push.
