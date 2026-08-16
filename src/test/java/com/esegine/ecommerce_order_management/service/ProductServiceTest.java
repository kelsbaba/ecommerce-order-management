package com.esegine.ecommerce_order_management.service;


import com.esegine.ecommerce_order_management.entity.Product;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_shouldSaveAndReturnedProduct() {

        Product product = new Product("Laptop",
                "Dell laptop", new BigDecimal("750000"),10);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals(product,result);

        verify(productRepository).save(product);
    }

    @Test
    void getProductById_shouldReturnProductWhenFound() {

        Long productId = 1L;

        Product product = new Product("Laptop","Dell laptop",
                new BigDecimal("750000"), 10);

        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(product,result);
        assertEquals(productId,result.getId());

        verify(productRepository).findById(productId);
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.getProductById(productId));

        assertEquals("Product not found.",exception.getMessage());
        verify(productRepository).findById(productId);
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {

        List<Product> products = List.of(new Product("Laptop","Dell laptop",
                new BigDecimal("750000"),10), new Product("Phone","Samsung smartphone",
               new BigDecimal("450000"),15 ));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(products,result);

        verify(productRepository).findAll();
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {

        Long productId = 1L;

        Product existingProduct = new Product(
                "Laptop",
                "Old laptop description",
                new BigDecimal("700000"),
                5
        );
        existingProduct.setId(productId);

        Product updatedProduct = new Product(
                "Gaming Laptop",
                "Updated gaming laptop",
                new BigDecimal("950000"),
                10
        );

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertNotNull(result);
        assertEquals("Gaming Laptop", result.getName());
        assertEquals("Updated gaming laptop", result.getDescription());
        assertEquals(new BigDecimal("950000"), result.getPrice());
        assertEquals(10, result.getStockQuantity());

        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;
        Product updatedProduct = new Product(
                "Gaming laptop",
                "Updated gaming laptop",
                new BigDecimal("950000"),
                10
        );
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.updateProduct(productId, updatedProduct));
        assertEquals("Product not found.", exception.getMessage());

        verify(productRepository).findById(productId);
    }

    @Test
    void deleteProduct_shouldDeleteProductWhenFound() {
        Long productId = 1L;
        Product existingProduct = new Product(
                "Laptop", "Dell laptop",
                new BigDecimal("750000"), 10);

        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(productId);
        verify(productRepository).findById(productId);
        verify(productRepository).delete(existingProduct);
    }

    @Test
    void deleteProduct_shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> productService.deleteProduct(productId)
        );

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(productId);
    }
}
