package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.service.CartItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemControllerTest {

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartItemController cartItemController;

    @Test
    void createCartItem_shouldReturnCreatedCartItem() {
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = new CartItem(cart, product, 2);

        when(cartItemService.createCartItem(cartItem)).thenReturn(cartItem);

        CartItem result = cartItemController.createCartItem(cartItem);
        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());

        verify(cartItemService).createCartItem(cartItem);

    }


    @Test
    void getCartItem_shouldReturnCartItem() {
        Long cartItemId = 1L;
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = new CartItem(cart, product, 2);
        cartItem.setId(cartItemId);

        when(cartItemService.getCartItemById(cartItemId)).thenReturn(cartItem);

        CartItem result = cartItemController.getCartItem(cartItemId);

        assertNotNull(result);
        assertEquals(cartItemId, result.getId());
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());

        verify(cartItemService).getCartItemById(cartItemId);
    }

    @Test
    void getCartItem_shouldThrowException_WhenCartItemNotFound() {
        Long cartItemId = 99L;

        when(cartItemService.getCartItemById(cartItemId))
                .thenThrow(new RuntimeException("CartItem not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemController.getCartItem(cartItemId));

        assertEquals("CartItem not found", exception.getMessage());

        verify(cartItemService).getCartItemById(cartItemId);
    }

    @Test
    void getAllCartItems_shouldReturnAllCartItems() {
        Cart cart1 = new Cart();
        cart1.setId(1L);

        Cart cart2 = new Cart();
        cart2.setId(2L);

        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);

        CartItem cartItem1 = new CartItem(cart1, product1, 2);
        CartItem cartItem2 = new CartItem(cart2, product2, 3);

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);

        when(cartItemService.getAllCartItems()).thenReturn(cartItems);

        List<CartItem> result = cartItemController.getAllCartItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(cart1, result.get(0).getCart());
        assertEquals(product1, result.get(0).getProduct());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(cart2, result.get(1).getCart());
        assertEquals(product2, result.get(1).getProduct());
        assertEquals(3, result.get(1).getQuantity());

        verify(cartItemService).getAllCartItems();
    }

    @Test
    void updateCartItem_shouldReturnUpdatedCartItem() {
        Long cartItemId = 1L;
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);

        CartItem updatedCartItem = new CartItem(cart, product, 5);
        updatedCartItem.setId(cartItemId);

        when(cartItemService.updateCartItem(cartItemId, updatedCartItem))
                .thenReturn(updatedCartItem);

        CartItem result = cartItemController.updateCartItem(cartItemId, updatedCartItem);

        assertNotNull(result);
        assertEquals(cartItemId, result.getId());
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(5, result.getQuantity());

        verify(cartItemService).updateCartItem(cartItemId, updatedCartItem);
    }

    @Test
    void updateCartItem_shouldThrowException_WhenCartItemNotFound() {
        Long cartItemId = 99L;
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);

        CartItem updatedCartItem = new CartItem(cart, product, 5);

        when(cartItemService.updateCartItem(cartItemId, updatedCartItem))
                .thenThrow(new RuntimeException("CartItem not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemController.updateCartItem(cartItemId, updatedCartItem)
        );

        assertEquals("CartItem not found", exception.getMessage());

        verify(cartItemService).updateCartItem(cartItemId, updatedCartItem);
    }

    @Test
    public void deleteCartItem_shouldCallService() {
        Long cartItemId = 1L;

        cartItemController.deleteCartItem(cartItemId);

        verify(cartItemService).deleteCartItem(cartItemId);
    }

    @Test
    void deleteCartItem_shouldThrowException_whenCartItemNotFound() {
        Long cartItemId = 99L;

        doThrow(new RuntimeException("CartItem not found"))
                .when(cartItemService)
                .deleteCartItem(cartItemId);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemController.deleteCartItem(cartItemId)
        );

        assertEquals("CartItem not found", exception.getMessage());

        verify(cartItemService).deleteCartItem(cartItemId);
    }

}