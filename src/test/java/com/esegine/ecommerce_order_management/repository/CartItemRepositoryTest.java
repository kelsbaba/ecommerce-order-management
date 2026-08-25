package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.entity.Category;
import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindCartItem() {

        User user = new User(
                "John",
                "Doe",
                "john2@example.com"
        );

        User savedUser = userRepository.save(user);

        Cart cart = new Cart(savedUser);

        Cart savedCart = cartRepository.save(cart);

        Category category = new Category("Electronics");

        Category savedCategory = categoryRepository.save(category);

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("1500.00"),
                10
        );

        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);

        CartItem cartItem = new CartItem(
                savedCart,
                savedProduct,
                2
        );

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        assertNotNull(savedCartItem.getId());

        CartItem foundCartItem = cartItemRepository
                .findById(savedCartItem.getId())
                .orElse(null);

        assertNotNull(foundCartItem);

        assertEquals(
                savedCart.getId(),
                foundCartItem.getCart().getId()
        );

        assertEquals(
                savedProduct.getId(),
                foundCartItem.getProduct().getId()
        );

        assertEquals(2, foundCartItem.getQuantity());
    }
}