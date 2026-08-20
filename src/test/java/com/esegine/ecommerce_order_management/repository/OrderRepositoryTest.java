package com.esegine.ecommerce_order_management.repository;

import com.esegine.ecommerce_order_management.entity.Order;
import com.esegine.ecommerce_order_management.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndFindOrder() {

        Order order = new Order(
                LocalDateTime.of(2026, 8, 20, 10, 30),
                OrderStatus.PENDING,
                new BigDecimal("2500.00")
        );

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        assertNotNull(foundOrder);
        assertEquals(
                LocalDateTime.of(2026, 8, 20, 10, 30),
                foundOrder.getOrderDate()
        );
        assertEquals(OrderStatus.PENDING, foundOrder.getStatus());
        assertEquals(new BigDecimal("2500.00"), foundOrder.getTotalAmount());
    }
}