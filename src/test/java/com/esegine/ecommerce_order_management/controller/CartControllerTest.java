package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    void createCart_shouldReturnCreatedCart() {
        Long cartId = 1L;

        User user = new User();
        user.setId(cartId);

        Cart cart = new Cart(user);

        when(cartService.createCart(cart)).thenReturn(cart);

        Cart result = cartController.createCart(cart);

        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(cartService).createCart(cart);
    }

    @Test
    void getCartCartById_shouldReturnCart() {
        Long cartId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart(user);
        cart.setId(cartId);

        when(cartService.getCartById(cartId)).thenReturn(cart);

        Cart result = cartController.getCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(user, result.getUser());

        verify(cartService).getCartById(cartId);
    }

    @Test
    void getCartById_shouldThrowException_WhenCartNotFound() {
        Long cartId = 99L;

        when(cartService.getCartById(cartId)).thenThrow(new RuntimeException("Cart not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartController.getCartById(cartId));

        assertEquals("Cart not found", exception.getMessage());

        verify(cartService).getCartById(cartId);
    }

    @Test
    void getCartByUserId_shouldReturnCart() {
        Long userId = 1L;
        Long cartId = 1L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart(user);
        cart.setId(cartId);

        when(cartService.getCartByUserId(userId)).thenReturn(cart);

        Cart result = cartController.getCartByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user, result.getUser());

        verify(cartService).getCartByUserId(userId);
    }

    @Test
    void getCartByUserId_shouldThrowException_whenCartNotFound() {
        Long userId = 1L;

        when(cartService.getCartByUserId(userId)).thenThrow(
                new RuntimeException("Cart not found"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartController.getCartByUserId(userId));
        assertEquals("Cart not found", exception.getMessage());

        verify(cartService).getCartByUserId(userId);
    }

    @Test
    void getAllCarts_shouldReturnAllCarts() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        Cart cart1 = new Cart(user1);
        Cart cart2 = new Cart(user2);

        List<Cart> carts = List.of(cart1, cart2);

        when(cartService.getAllCarts()).thenReturn(carts);

        List<Cart> result = cartController.getAllCarts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1, result.get(0).getUser());
        assertEquals(user2, result.get(1).getUser());

        verify(cartService).getAllCarts();
    }

    @Test
    void updateCart_shouldReturnUpdatedCart() {
        Long cartId= 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart updatedCart = new Cart(user);
        updatedCart.setId(cartId);

        when(cartService.updateCart(cartId, updatedCart)).thenReturn(updatedCart);

        Cart result = cartController.updateCart(cartId, updatedCart);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(user, result.getUser());

        verify(cartService).updateCart(cartId, updatedCart);
    }

    @Test
    void updateCart_shouldThrowException_WhenCartNotFound() {
        Long cartId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart updatedCart = new Cart(user);

        when(cartService.updateCart(cartId, updatedCart)).thenThrow(
                new RuntimeException("Cart not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartController.updateCart(cartId, updatedCart));

        assertEquals("Cart not found", exception.getMessage());

        verify(cartService).updateCart(cartId, updatedCart);
    }

    @Test
    void deleteCart_shouldCallService() {
        Long cartId = 1L;

        cartController.deleteCart(cartId);

        verify(cartService).deleteCart(cartId);
    }

    @Test
    void deleteCart_shouldThrowException_whenCartNotFound() {
        Long cartId = 99L;
        doThrow(new RuntimeException("Cart not found"))
                .when(cartService)
                .deleteCart(cartId);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartController.deleteCart(cartId));

        assertEquals("Cart not found", exception.getMessage());
        verify(cartService).deleteCart(cartId);
    }
}
