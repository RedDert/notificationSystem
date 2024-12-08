package com.reddert.notificationsystem.user.service;

import com.reddert.notificationsystem.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.reddert.notificationsystem.user.dtos.CreateUserDTO;
import com.reddert.notificationsystem.user.dtos.UserDTO;
import com.reddert.notificationsystem.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        // Check for duplicate email
        if (userRepository.findByEmail(createUserDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already associated with an existing user.");
        }

        User user = new User(createUserDTO.name(), createUserDTO.email());
        User savedUser = userRepository.save(user);
        logger.info("Created new user with ID: {}", savedUser.getId());
        return UserDTO.fromEntity(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    @Transactional
    public UserDTO updateUser(UUID id, CreateUserDTO createUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        userRepository.findByEmail(createUserDTO.email())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("Email is already associated with another user.");
                });

        user.setName(createUserDTO.name());
        user.setEmail(createUserDTO.email());
        User updatedUser = userRepository.save(user);
        logger.info("Updated user with ID: {}", id);
        return UserDTO.fromEntity(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent user with ID: {}", id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("Deleted user with ID: {}", id);
    }
}
