package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Order;
import com.esegine.ecommerce_order_management.enums.OrderStatus;
import com.esegine.ecommerce_order_management.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @Test
    void createOrder_shouldReturnCreatedOrder() {

        LocalDateTime orderDate = LocalDateTime.of(2026, 8, 19, 10, 0);

        Order order = new Order(
                orderDate,
                OrderStatus.CONFIRMED,
                new BigDecimal("2500.00")
        );

        when(orderService.createOrder(order)).thenReturn(order);

        ResponseEntity<Order> response = orderController.createOrder(order);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Order result = response.getBody();

        assertNotNull(result);
        assertEquals(orderDate, result.getOrderDate());

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals(new BigDecimal("2500.00"), result.getTotalAmount());

        verify(orderService).createOrder(order);

    }

    @Test
    void getOrderById_shouldReturnOrder() {

        Long orderId =1L;

        LocalDateTime orderDate = LocalDateTime.of(2026, 8, 19, 10, 0);

        Order order = new Order(
                orderDate,
                OrderStatus.CONFIRMED,
                new BigDecimal("2500.00")
        );
        order.setId(orderId);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Order result = response.getBody();

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(orderDate, result.getOrderDate());
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals(new BigDecimal("2500.00"), result.getTotalAmount());

        verify(orderService).getOrderById(orderId);
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenOrderNotFound() {

        Long orderId = 99L;

        when(orderService.getOrderById(orderId)).thenThrow(
                new RuntimeException("Order not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderController.getOrderById(orderId));

        assertEquals("Order not found", exception.getMessage());

        verify(orderService).getOrderById(orderId);
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {

        Order order1 = new Order(
                LocalDateTime.of(2023, 8, 19, 10, 0),
                OrderStatus.CONFIRMED,
                new BigDecimal("2500.00")
        );
        Order order2 = new Order(
                LocalDateTime.of(2026, 8, 19, 11, 0),
                OrderStatus.PENDING,
                new BigDecimal("1500.00")
        );
        List<Order> orders = List.of(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        List<Order> result = response.getBody();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(OrderStatus.CONFIRMED, result.get(0).getStatus());
        assertEquals(OrderStatus.PENDING, result.get(1).getStatus());
        assertEquals(new BigDecimal("2500.00"), result.get(0).getTotalAmount());
        assertEquals(new BigDecimal("1500.00"), result.get(1).getTotalAmount());

        verify(orderService).getAllOrders();
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrder() {

        Long orderId = 1L;

        Order updatedOrder = new Order(
                LocalDateTime.of(2026, 8, 19, 12, 0),
                OrderStatus.SHIPPED,
                new BigDecimal("3000.00")
        );

        updatedOrder.setId(orderId);

        when(orderService.updateOrder(orderId, updatedOrder)).thenReturn(updatedOrder);

        ResponseEntity<Order> response = orderController.updateOrder(orderId, updatedOrder);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Order result = response.getBody();

        assertNotNull(result);
        assertEquals(orderId, result.getId());

        assertEquals(LocalDateTime.of(2026,8, 19, 12, 0), result.getOrderDate());
        assertEquals(OrderStatus.SHIPPED, result.getStatus());

        assertEquals(new BigDecimal("3000.00"), result.getTotalAmount());

        verify(orderService).updateOrder(orderId, updatedOrder);
    }

    @Test
    void updateOrder_shouldThrowExceptionWhenOrderNotFound()
    {

        Long orderId = 99L;

        Order updatedOrder = new Order(
                LocalDateTime.of(2026, 8, 19, 12, 0),
                OrderStatus.SHIPPED,
                new BigDecimal("3000.00")
        );

        when(orderService.updateOrder(orderId,updatedOrder)).thenThrow(
                new RuntimeException("Order not found")
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderController.updateOrder(orderId, updatedOrder));
        assertEquals("Order not found", exception.getMessage());

        verify(orderService).updateOrder(orderId, updatedOrder);
    }

    @Test
    void deleteOrder_shouldReturnNoContent() {

        Long orderId = 1L;

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(orderService).deleteOrder(orderId);
    }
}
