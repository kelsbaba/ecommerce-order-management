package com.esegine.ecommerce_order_management.service;


import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldReturnSavedUser() {
        User user = new User(
                "John",
                "Doe",
                "john@example.com"
        );
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_shouldReturnUser_WhenUserExists() {

        Long userId = 1L;

        User user = new User(
                "John",
                "Doe",
                "john@example.com"
        );
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_ThrowException_WhenNotFound() {

        Long userid = 1L;

        when(userRepository.findById(userid)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userid));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userid);
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

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser_WhenUserExists() {

        Long userid = 1L;
        User existingUser = new User(
                "John",
                "Doe",
                "john@example.com"
        );
        existingUser.setId(userid);

        User updatedUser = new User(
                "Johnny",
                "Doe",
                "johnny@example.com"
        );
        when(userRepository.findById(userid)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser)).thenReturn(existingUser);



        User result = userService.updateUser(userid, updatedUser);

        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johnny@example.com", result.getEmail());

        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_shouldThrowException_WhenUserDoesNotExist() {

        Long userId = 1L;

        User updateduser = new User(
                "Johnny",
                "Doe",
                "johnny@example.com"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()-> userService.updateUser(userId, updateduser));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser_WhenUserExists() {

        Long userId = 1L;

        User user = new User(
                "John",
                "Doe",
                "john@example.com"
        );
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_WhenUserDoesNotExist() {

        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

}


