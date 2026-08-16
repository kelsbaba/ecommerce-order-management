package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
