package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Order;
import com.esegine.ecommerce_order_management.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_shouldCreateSuccessfully() {

        Order order = new Order();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals(order,result);

        verify(orderRepository,times(1)).save(order);
    }

    @Test
    void getOrderById_shouldReturnOrderSuccess() {

        Long orderId = 1L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(order,result);

        verify(orderRepository,times(1)).findById(orderId);
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenOrderNotFound() {

        Long orderId =1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () ->orderService.getOrderById(orderId));

        assertEquals("Order not found",exception.getMessage());

        verify(orderRepository,times(1)).findById(orderId);
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {

        Order order1 = new Order();
        Order order2 = new Order();

        List<Order> orders = List.of(order1,order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orders, result);

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void updateOrder_shouldUpdateSuccessfully() {

        Long orderId = 1L;

        Order existingOrder = new Order();
        Order updatedOrder = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.updateOrder(orderId, updatedOrder);

        assertNotNull(result);
        assertEquals(existingOrder, result);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void updateOrder_shouldThrowExceptionWhenOrderNotFound() {

        Long orderId = 1L;
        Order updatedOrder = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.updateOrder(orderId, updatedOrder));

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deleteOrder_shouldDeleteSuccessfully() {

        Long orderId = 1L;

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository,times(1)).deleteById(orderId);
    }

}
