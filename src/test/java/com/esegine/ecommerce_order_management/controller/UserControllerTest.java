package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void createUser_shouldReturnCreatedUser() {
        User user = new User(
                "John",
                "Doe",
                "john@example.com"
        );

        when(userService.createUser(user)).thenReturn(user);

        User result = userController.createUser(user);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());

        verify(userService).createUser(user);
    }

    @Test
    void getUserById_shouldReturnUser() {
        Long userId = 1L;

        User user = new User(
                "John",
                "Doe",
                "john@example.com"
        );
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(user);

        User result = userController.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());

        verify(userService).getUserById(userId);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        Long userId = 99L;

        when(userService.getUserById(userId))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.getUserById(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userService).getUserById(userId);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user1 = new User(
                "John",
                "Doe",
                "john@example.com"
        );

        User user2 = new User(
                "Jane",
                "Smith",
                "jane@example.com"
        );

        List<User> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(userService).getAllUsers();
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        Long userId = 1L;

        User updatedUser = new User(
                "Updated John",
                "Updated Doe",
                "updated@example.com"
        );
        updatedUser.setId(userId);

        when(userService.updateUser(userId, updatedUser))
                .thenReturn(updatedUser);

        User result = userController.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Updated John", result.getFirstName());
        assertEquals("Updated Doe", result.getLastName());
        assertEquals("updated@example.com", result.getEmail());

        verify(userService).updateUser(userId, updatedUser);
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        Long userId = 99L;

        User updatedUser = new User(
                "Updated John",
                "Updated Doe",
                "updated@example.com"
        );

        when(userService.updateUser(userId, updatedUser))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userController.updateUser(userId, updatedUser)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userService).updateUser(userId, updatedUser);
    }

    @Test
    void deleteUser_shouldCallService() {
        Long userId = 1L;

        userController.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }
}