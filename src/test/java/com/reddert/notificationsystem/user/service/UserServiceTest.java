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
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "lionel.messi@gmail.com");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        UserDTO result = userService.createUser(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Lionel Messi", result.name());
        assertEquals("lionel.messi@gmail.com", result.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_withEmptyName_shouldThrowException() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO("", "lionel.messi@gmail.com");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withInvalidCharactersInName_shouldThrowException() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel@Messi", "lionel.messi@gmail.com");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withTooLongName_shouldThrowException() {
        // Arrange
        String longName = "a".repeat(101);
        CreateUserDTO createUserDTO = new CreateUserDTO(longName, "lionel.messi@gmail.com");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withDuplicateEmail_shouldThrowException() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "lionel.messi@gmail.com");
        when(userRepository.findByEmail("lionel.messi@gmail.com")).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void createUser_withInvalidEmail_shouldThrowException() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO("Lionel Messi", "invalid-email");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void getAllUsers_shouldReturnListOfUserDTOs() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Lionel Messi", result.getFirst().name());
        assertEquals("lionel.messi@gmail.com", result.getFirst().email());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserDTO() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        UserDTO result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals("Lionel Messi", result.name());
        assertEquals("lionel.messi@gmail.com", result.email());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_shouldReturnUpdatedUserDTO() {
        // Arrange
        CreateUserDTO updatedUserDTO = new CreateUserDTO("Lionel Messi Updated", "lionel.messi.updated@gmail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        UserDTO result = userService.updateUser(userId, updatedUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Lionel Messi Updated", result.name());
        assertEquals("lionel.messi.updated@gmail.com", result.email());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void deleteUser_shouldInvokeRepositoryDeleteById() {
        // Arrange
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
