package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
