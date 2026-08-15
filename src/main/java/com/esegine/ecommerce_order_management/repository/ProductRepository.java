package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
