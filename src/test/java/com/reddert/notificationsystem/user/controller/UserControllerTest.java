package com.reddert.notificationsystem.user.controller;

import com.reddert.notificationsystem.user.dtos.CreateUserDTO;
import com.reddert.notificationsystem.user.dtos.UserDTO;
import com.reddert.notificationsystem.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserDTO(UUID.randomUUID(), "Lionel Messi", "lionel.messi@gmail.com");
        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(mockUser);
        when(userService.getAllUsers()).thenReturn(List.of(mockUser));
        when(userService.getUserById(any(UUID.class))).thenReturn(mockUser);
        when(userService.updateUser(any(UUID.class), any(CreateUserDTO.class))).thenReturn(mockUser);
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        String requestBody = "{\"name\":\"Lionel Messi\",\"email\":\"lionel.messi@gmail.com\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lionel Messi"))
                .andExpect(jsonPath("$.email").value("lionel.messi@gmail.com"));
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Lionel Messi"))
                .andExpect(jsonPath("$[0].email").value("lionel.messi@gmail.com"));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UUID userId = mockUser.id();

        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lionel Messi"))
                .andExpect(jsonPath("$.email").value("lionel.messi@gmail.com"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UUID userId = mockUser.id();
        String requestBody = "{\"name\":\"Lionel Messi Updated\",\"email\":\"lionel.messi.updated@gmail.com\"}";

        when(userService.updateUser(eq(userId), any(CreateUserDTO.class)))
                .thenReturn(new UserDTO(userId, "Lionel Messi Updated", "lionel.messi.updated@gmail.com"));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lionel Messi Updated"))
                .andExpect(jsonPath("$.email").value("lionel.messi.updated@gmail.com"));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        UUID userId = mockUser.id();

        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
