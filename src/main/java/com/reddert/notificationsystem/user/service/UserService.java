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
}