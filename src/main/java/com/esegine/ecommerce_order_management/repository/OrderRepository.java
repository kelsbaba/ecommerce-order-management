package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
