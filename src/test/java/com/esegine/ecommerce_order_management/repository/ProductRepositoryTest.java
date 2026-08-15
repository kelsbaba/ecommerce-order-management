package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct() {

        Product product = new Product("Laptop", "High-performance laptop",
                new BigDecimal("1500.00"), 10);

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        assertNotNull(foundProduct);
        assertEquals("Laptop", foundProduct.getName());
        assertEquals("High-performance laptop", foundProduct.getDescription());
        assertEquals(new BigDecimal("1500.00"), foundProduct.getPrice());
        assertEquals(10, foundProduct.getStockQuantity());

    }
}
