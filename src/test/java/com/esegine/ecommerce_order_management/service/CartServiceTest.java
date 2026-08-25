package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void createCart_shouldSaveAndReturnCart() {
        User user = new User(
                "John",
                "Doe",
                "john3@example.com"
        );
        Cart cart = new Cart(user);

        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.createCart(cart);

        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(cartRepository).save(cart);
    }

    @Test
    void getCartById_shouldReturnCart_WhenCartExist() {
        Long cartId = 1L;
        User user = new User(
                "John",
                "Doe",
                "john4@example.com"
        );
        Cart cart = new Cart(user);
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(user, result.getUser());

        verify(cartRepository).findById(cartId);

    }

    @Test
    void getCartById_shouldThrowException_WhenCartDoesNotExist() {
        Long cartId = 99L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.getCartById(cartId));
        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(cartId);
    }

    @Test
    void getAllCarts_shouldReturnAllCarts() {
        User user1 = new User("John", "Doe", "john5@example.com");
        User user2 = new User("Jane", "Doe", "jane1@example.com");

        Cart cart1 = new Cart(user1);
        Cart cart2 = new Cart(user2);

        List<Cart> carts = List.of(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(carts);

        List<Cart> result = cartService.getAllCarts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(carts, result);

        verify(cartRepository).findAll();
    }

    @Test
    void updateCart_shouldUpdateAndReturnCart() {
        Long cartId = 1L;
        User existinguser = new User(
                "John",
                "Doe",
                "john6@example.com");

        User updatedUser = new User(
                "Jane",
                "Doe",
                "jane2@example.com");

        Cart existingCart = new Cart(existinguser);
        existingCart.setId(cartId);

        Cart updatedCart = new Cart(updatedUser);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));

        when(cartRepository.save(existingCart)).thenReturn(existingCart);

        Cart result = cartService.updateCart(cartId, updatedCart);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(updatedUser, result.getUser());

        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(existingCart);
    }

    @Test
    void updateCart_shouldThrowException_WhenCartDoesNotExist() {

        Long cartId = 99L;

        Cart updatedCart = new Cart(
                new User(
                        "Jane",
                        "Doe",
                        "jane3@example.com"
                )
        );

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.updateCart(cartId, updatedCart)
        );

        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(cartId);
    }

    @Test
    void deleteCart_shouldDeleteCart_WhenCartExists() {

        Long cartId = 1L;

        Cart cart = new Cart(
                new User(
                        "John",
                        "Doe",
                        "john7@example.com"
                )
        );

        cart.setId(cartId);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        cartService.deleteCart(cartId);

        verify(cartRepository).findById(cartId);
        verify(cartRepository).delete(cart);
    }

    @Test
    void deleteCart_shouldThrowException_WhenCartDoesNotExist() {

        Long cartId = 99L;

        when(cartRepository.findById(cartId))
                .thenReturn(java.util.Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> cartService.deleteCart(cartId)
        );

        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(cartId);
    }
}
