package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.entity.User;
import com.esegine.ecommerce_order_management.repository.CartItemRepository;
import com.esegine.ecommerce_order_management.repository.CartRepository;
import com.esegine.ecommerce_order_management.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void addProductToCart_shouldThrowException_WhenQuantityIsLessThanOne() {
        Long cartId = 1L;
        Long productId = 1L;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.addProductToCart(cartId, productId, 0)
        );

        assertEquals("Quantity most be at least 1", exception.getMessage());

        verifyNoInteractions(
                cartRepository,
                productRepository,
                cartItemRepository
        );
    }

    @Test
    void addProductToCart_shouldThrowException_WhenCartDoesNotExist() {
        Long cartId = 99L;
        Long productId = 1L;

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.addProductToCart(cartId, productId, 2)
        );

        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(cartId);

        verifyNoInteractions(
                productRepository,
                cartItemRepository
        );
    }

    @Test
    void addProductToCart_shouldThrowException_WhenProductDoesNotExist() {
        Long cartId = 1L;
        Long productId = 99L;

        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.addProductToCart(cartId, productId, 2)
        );

        assertEquals("Product not found", exception.getMessage());

        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);

        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void addProductToCart_shouldAddNewCartItem() {
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                10
        );
        product.setId(productId);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.empty());

        CartItem savedCartItem = new CartItem(cart, product, quantity);

        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(savedCartItem);

        CartItem result = cartService.addProductToCart(
                cartId,
                productId,
                quantity
        );

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(quantity, result.getQuantity());

        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);
        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addProductToCart_shouldIncreaseQuantity_WhenProductAlreadyInCart() {
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                10
        );
        product.setId(productId);

        CartItem existingCartItem = new CartItem(cart, product, 2);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(existingCartItem));

        when(cartItemRepository.save(existingCartItem))
                .thenReturn(existingCartItem);

        CartItem result = cartService.addProductToCart(
                cartId,
                productId,
                3
        );

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(5, result.getQuantity());

        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);
        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void addProductToCart_shouldThrowException_WhenNewQuantityExceedsStock() {
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                5
        );
        product.setId(productId);

        CartItem existingCartItem = new CartItem(cart, product, 3);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(existingCartItem));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.addProductToCart(cartId, productId, 3)
        );

        assertEquals("Insufficient stock", exception.getMessage());

        assertEquals(3, existingCartItem.getQuantity());

        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);
        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository, never())
                .save(any(CartItem.class));
    }

    @Test
    void addProductToCart_shouldThrowException_WhenRequestedQuantityExceedsStock() {
        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                5
        );
        product.setId(productId);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.addProductToCart(cartId, productId, 6)
        );

        assertEquals("Insufficient stock", exception.getMessage());

        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);

        verify(cartItemRepository, never())
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository, never())
                .save(any(CartItem.class));
    }

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
    void getCartByUserId_shouldReturnCart() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart(user);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(userId);
        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void getCartByUserId_shouldThrowException_whenCartNotFound() {
        Long userId = 99L;

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.getCartByUserId(userId));
        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findByUserId(userId);
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

    @Test
    void decreaseProductQuantity_shouldThrowException_WhenQuantityIsLessThanOne() {

        Long cartId = 1L;
        Long productId = 1L;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.decreaseProductQuantity(cartId, productId, 0)
        );

        assertEquals("Quantity must be at least 1", exception.getMessage());

        verifyNoInteractions(
                cartRepository,
                cartItemRepository
        );
    }

    @Test
    void decreaseProductQuantity_shouldThrowException_WhenCartDoesNotExist() {

        Long cartId = 99L;
        Long productId = 1L;

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.decreaseProductQuantity(cartId, productId, 2)
        );

        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(cartId);

        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void decreaseProductQuantity_shouldThrowException_WhenCartItemDoesNotExist() {

        Long cartId = 1L;
        Long productId = 99L;

        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.decreaseProductQuantity(cartId, productId, 2)
        );

        assertEquals("Cart item not found", exception.getMessage());

        verify(cartRepository).findById(cartId);

        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository, never())
                .save(any(CartItem.class));

        verify(cartItemRepository, never())
                .delete(any(CartItem.class));
    }

    @Test
    void decreaseProductQuantity_shouldDecreaseQuantity_WhenQuantityRemainsAboveZero() {

        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                10
        );
        product.setId(productId);

        CartItem cartItem = new CartItem(cart, product, 5);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(cartItem))
                .thenReturn(cartItem);

        CartItem result = cartService.decreaseProductQuantity(
                cartId,
                productId,
                2
        );

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(3, result.getQuantity());

        verify(cartRepository).findById(cartId);

        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository).save(cartItem);

        verify(cartItemRepository, never())
                .delete(any(CartItem.class));
    }

    @Test
    void decreaseProductQuantity_shouldDeleteCartItem_WhenQuantityBecomesZero() {

        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                10
        );
        product.setId(productId);

        CartItem cartItem = new CartItem(cart, product, 2);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(cartItem));

        CartItem result = cartService.decreaseProductQuantity(
                cartId,
                productId,
                2
        );

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());

        verify(cartRepository).findById(cartId);

        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository).delete(cartItem);

        verify(cartItemRepository, never())
                .save(any(CartItem.class));
    }

    @Test
    void decreaseProductQuantity_shouldThrowException_WhenQuantityWouldGoBelowZero() {

        Long cartId = 1L;
        Long productId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("500000.00"),
                10
        );
        product.setId(productId);

        CartItem cartItem = new CartItem(cart, product, 2);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId))
                .thenReturn(Optional.of(cartItem));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.decreaseProductQuantity(
                        cartId,
                        productId,
                        3
                )
        );

        assertEquals(
                "Cannot decrease quantity below zero",
                exception.getMessage()
        );

        assertEquals(2, cartItem.getQuantity());

        verify(cartRepository).findById(cartId);

        verify(cartItemRepository)
                .findByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository, never())
                .save(any(CartItem.class));

        verify(cartItemRepository, never())
                .delete(any(CartItem.class));
    }
}
