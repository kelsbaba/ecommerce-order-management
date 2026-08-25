package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindCart() {

        User user = new User(
                "John",
                "Doe",
                "john1@example.com"
        );

        User savedUser = userRepository.save(user);

        Cart cart = new Cart(savedUser);

        Cart savedCart = cartRepository.save(cart);

        assertNotNull(savedCart.getId());

        Cart foundCart = cartRepository
                .findById(savedCart.getId())
                .orElse(null);

        assertNotNull(foundCart);
        assertEquals(savedUser.getId(), foundCart.getUser().getId());
    }
}