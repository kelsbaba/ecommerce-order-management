package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
}
