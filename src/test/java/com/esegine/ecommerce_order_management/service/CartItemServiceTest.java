package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    void createCartItem_shouldSaveAndReturnCartItem() {
        User user = new User("John", "Doe", "john8@example.com");
        Cart cart = new Cart(user);

        Product product = new Product("Laptop", "Gaming laptop",
                new BigDecimal("1500.00"), 10);
        CartItem cartItem = new CartItem(cart, product, 2);

        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        CartItem result = cartItemService.createCartItem(cartItem);

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());

        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void getCartItemById_shouldReturnCartItem_WhenCartItemExists() {
        Long cartItemId =1L;

        User user = new User(
                "John",
                "Doe",
                "john9@example.com"
        );

        Cart cart = new Cart(user);

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("1500.00"),
                10
        );

        CartItem cartItem = new CartItem(
                cart,
                product,
                2
        );

        cartItem.setId(cartItemId);

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.getCartItemById(cartItemId);

        assertNotNull(result);
        assertEquals(cartItemId, result.getId());
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());

        verify(cartItemRepository).findById(cartItemId);
    }

    @Test
    void getCartItemById_shouldThrowException_WhenCartItemDoesNotExist() {

        Long cartItemId = 99L;

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemService.getCartItemById(cartItemId));

        assertEquals("Cart item not found", exception.getMessage());

        verify(cartItemRepository).findById(cartItemId);
    }

    @Test
    void getAllCartItems_shouldReturnAllCartItems() {

        User user = new User(
                "John",
                "Doe",
                "john10@example.com"
        );

        Cart cart = new Cart(user);

        Product product1 = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("1500.00"),
                10
        );

        Product product2 = new Product(
                "Phone",
                "Smart Phone",
                new BigDecimal("800.00"),
                20
        );

        CartItem cartItem1 = new CartItem(
                cart,
                product1,
                2
        );

        CartItem cartItem2 = new CartItem(
                cart,
                product2,
                1
        );

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);

        when(cartItemRepository.findAll())
                .thenReturn(cartItems);

        List<CartItem> result =
                cartItemService.getAllCartItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(cartItems, result);

        verify(cartItemRepository).findAll();
    }

    @Test
    void updateCartItem_shouldUpdateAndReturnCartItem_WhenCartItemExists() {

        Long cartItemId = 1L;

        User user = new User(
                "John",
                "Doe",
                "john11@example.com"
        );

        Cart cart = new Cart(user);

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("1500.00"),
                10
        );

        CartItem existingCartItem = new CartItem(
                cart,
                product,
                2
        );

        existingCartItem.setId(cartItemId);

        CartItem updatedCartItem = new CartItem(
                cart,
                product,
                5
        );

        updatedCartItem.setId(cartItemId);

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.of(existingCartItem));

        when(cartItemRepository.save(existingCartItem))
                .thenReturn(existingCartItem);

        CartItem result =
                cartItemService.updateCartItem(cartItemId, updatedCartItem);

        assertNotNull(result);
        assertEquals(cartItemId, result.getId());
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(5, result.getQuantity());

        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void updateCartItem_shouldThrowException_WhenCartItemDoesNotExist() {

        Long cartItemId = 99L;

        CartItem updatedCartItem = new CartItem();

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemService.updateCartItem(cartItemId, updatedCartItem)
        );

        assertEquals("Cart item not found", exception.getMessage());

        verify(cartItemRepository).findById(cartItemId);
    }

    @Test
    void deleteCartItem_shouldDeleteCartItem_WhenCartItemExists() {

        Long cartItemId = 1L;

        User user = new User(
                "John",
                "Doe",
                "john12@example.com"
        );

        Cart cart = new Cart(user);

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("1500.00"),
                10
        );

        CartItem cartItem = new CartItem(
                cart,
                product,
                2
        );

        cartItem.setId(cartItemId);

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.of(cartItem));

        cartItemService.deleteCartItem(cartItemId);

        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void deleteCartItem_shouldThrowException_WhenCartItemDoesNotExist() {

        Long cartItemId = 99L;

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartItemService.deleteCartItem(cartItemId)
        );

        assertEquals("Cart item not found", exception.getMessage());

        verify(cartItemRepository).findById(cartItemId);
    }
}
