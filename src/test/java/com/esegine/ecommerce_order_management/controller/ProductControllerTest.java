package com.esegine.ecommerce_order_management.controller;


import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    ProductController productController;

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        Product product = new Product(
                "Laptop",
                "Dell laptop",
                new BigDecimal("1500.00"),
                10
                );
        when(productService.createProduct(product)).thenReturn(product);

        Product result = productController.createProduct(product);

        assertNotNull(result);
        assertEquals("Laptop",result.getName());
        assertEquals("Dell laptop",result.getDescription());
        assertEquals(new BigDecimal("1500.00"),result.getPrice());
        assertEquals(10,result.getStockQuantity());

        verify(productService).createProduct(product);

    }

    @Test
    void getProductById_shouldReturnProduct() {
        Long productId = 1L;
        Product product = new Product(
                "Laptop",
                "Dell laptop",
                new BigDecimal("1500.00"),
                10
        );
        product.setId(productId);

        when(productService.getProductById(productId)).thenReturn(product);

        Product result = productController.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId,result.getId());
        assertEquals("Laptop",result.getName());
        assertEquals("Dell laptop",result.getDescription());
        assertEquals(new BigDecimal("1500.00"),result.getPrice());
        assertEquals(10,result.getStockQuantity());

        verify(productService).getProductById(productId);
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductNotFound() {
        Long productId = 99L;

        when(productService.getProductById(productId)).thenThrow(new
                RuntimeException("Product not found"));
        RuntimeException exception = assertThrows(
                RuntimeException.class,() -> productController.getProductById(productId)
        );
        assertEquals("Product not found",exception.getMessage());

        verify(productService).getProductById(productId);
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        Product product1 = new Product(
                "Laptop",
                "Dell laptop",
                new BigDecimal("1500.00"),
                10
        );
        Product product2 = new Product(
                "Phone",
                "Samsung phone",
                new BigDecimal("800.00"),
                20
        );
        List<Product> products = List.of(product1,product2);

        when(productService.getAllProducts()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("Laptop",result.get(0).getName());
        assertEquals("Phone",result.get(1).getName());

        verify(productService).getAllProducts();
    }

    @Test
    void updateProduct_shouldReturnUpdateProduct() {
        Long productId = 1L;

        Product updatedProduct = new Product(
                "Updated Laptop",
                "Updated Dell Laptop",
                new BigDecimal("1800.00"),
                15
        );
        updatedProduct.setId(productId);

        when(productService.updateProduct(productId,updatedProduct)).thenReturn(updatedProduct);

        Product result = productController.updateProduct(productId,updatedProduct);

        assertNotNull(result);
        assertEquals(productId,result.getId());
        assertEquals("Updated Laptop",result.getName());
        assertEquals("Updated Dell Laptop",result.getDescription());
        assertEquals(new BigDecimal("1800.00"),result.getPrice());
        assertEquals(15,result.getStockQuantity());

        verify(productService).updateProduct(productId,updatedProduct);
    }

    @Test
    void updateProduct_shouldThroExceptionWhenProductNotFound() {

        Long productId = 99L;

        Product updatedProduct = new Product(
                "Updated Laptop",
                "Updated Dell Laptop",
                new BigDecimal("1800.00"),
                15
        );
        when(productService.updateProduct(productId,updatedProduct))
                .thenThrow(new RuntimeException("Product not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,() ->
                productController.updateProduct(productId,updatedProduct));

        assertEquals("Product not found",exception.getMessage());

        verify(productService).updateProduct(productId,updatedProduct);
    }

    @Test
    void deleteProduct_shouldCallService() {

        Long productId =1L;

        productController.deleteProduct(productId);

        verify(productService).deleteProduct(productId);
    }
}
