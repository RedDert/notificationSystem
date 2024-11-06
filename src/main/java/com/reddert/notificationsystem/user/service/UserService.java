package com.reddert.notificationsystem.user.service;

import com.reddert.notificationsystem.user.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.reddert.notificationsystem.user.dtos.CreateUserDTO;
import com.reddert.notificationsystem.user.dtos.UserDTO;
import com.reddert.notificationsystem.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        validateUserInput(createUserDTO.name(), createUserDTO.email());
        User user = new User(createUserDTO.name(), createUserDTO.email());
        User savedUser = userRepository.save(user);
        return UserDTO.fromEntity(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserDTO updateUser(UUID id, CreateUserDTO createUserDTO) {
        validateUserInput(createUserDTO.name(), createUserDTO.email());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(createUserDTO.name());
        user.setEmail(createUserDTO.email());
        User updatedUser = userRepository.save(user);
        return UserDTO.fromEntity(updatedUser);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    private void validateUserInput(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        if (!name.matches("^[A-Za-z ]+$")) {
            throw new IllegalArgumentException("User name contains invalid characters.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
