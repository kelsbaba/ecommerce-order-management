package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Category;
import com.esegine.ecommerce_order_management.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSavedAndFindCategory() {

        Category category = new Category("Electronics");

        Category savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory.getId());

        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertNotNull(foundCategory);
        assertEquals("Electronics", foundCategory.getName());
    }

    @Test
    @Transactional
    void shouldSaveCategoryWithMultipleProducts() {

        Category category = new Category("Test Electronics");

        Category savedCategory = categoryRepository.save(category);

        Product product1 = new Product(
                "Laptop",
                "High-performance laptop",
                new BigDecimal("1500.00"),
                10
        );
        Product product2 = new Product(
                "Desktop",
                "High-performance desktop",
                new BigDecimal("2000.00"),
                5
        );
        product1.setCategory(savedCategory);
        product2.setCategory(savedCategory);

        productRepository.save(product1);
        productRepository.save(product2);

        entityManager.flush();
        entityManager.clear();

        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertNotNull(foundCategory);
        assertEquals(2, foundCategory.getProducts().size());

        assertEquals(savedCategory.getId(), foundCategory.getProducts().get(0)
                .getCategory().getId());

        assertEquals(savedCategory.getId(), foundCategory.getProducts().get(1)
                .getCategory().getId());

    }
}
