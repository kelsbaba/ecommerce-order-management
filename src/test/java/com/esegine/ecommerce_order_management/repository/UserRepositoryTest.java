package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSavedAndFindUser() {

        //Create a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        // Save user to PostgreSQL
        User savedUser = userRepository.save(user);

        // Retrieve user from PostgreSQL
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Verify the user exist
        assertTrue(foundUser.isPresent());

        // Verify the data
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
        assertEquals("john.doe@example.com", foundUser.get().getEmail());


    }

}
