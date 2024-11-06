package com.reddert.notificationsystem.user.service;

import com.reddert.notificationsystem.user.dtos.CreateUserDTO;
import com.reddert.notificationsystem.user.dtos.UserDTO;
import com.reddert.notificationsystem.user.model.User;
import com.reddert.notificationsystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        mockUser = new User("Lionel Messi", "lionel.messi@gmail.com");
        mockUser.setId(userId);
    }

    @Test
    void createUser_shouldReturnUserDTO() {
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "lionel.messi@gmail.com");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserDTO result = userService.createUser(createUserDTO);

        assertNotNull(result);
        assertEquals("Lionel Messi", result.name());
        assertEquals("lionel.messi@gmail.com", result.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnListOfUserDTOs() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Lionel Messi", result.get(0).name());
        assertEquals("lionel.messi@gmail.com", result.get(0).email());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("Lionel Messi", result.name());
        assertEquals("lionel.messi@gmail.com", result.email());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_shouldReturnUpdatedUserDTO() {
        CreateUserDTO updatedUserDTO = new CreateUserDTO("Lionel Messi Updated", "lionel.messi.updated@gmail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        UserDTO result = userService.updateUser(userId, updatedUserDTO);

        assertNotNull(result);
        assertEquals("Lionel Messi Updated", result.name());
        assertEquals("lionel.messi.updated@gmail.com", result.email());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void deleteUser_shouldInvokeRepositoryDeleteById() {
        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    // Edge case tests
    @Test
    void createUser_withEmptyName_shouldThrowException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("", "lionel.messi@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withInvalidName_shouldThrowException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel123", "lionel.messi@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withEmptyEmail_shouldThrowException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withInvalidEmail_shouldThrowException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "invalid-email");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void updateUser_withInvalidEmail_shouldThrowException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "invalid-email");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, createUserDTO));
    }
}
