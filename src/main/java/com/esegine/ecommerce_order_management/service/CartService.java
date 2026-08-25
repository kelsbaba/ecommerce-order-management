package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);

    Cart getCartById(Long id);

    List<Cart> getAllCarts();

    Cart updateCart(Long id, Cart cart);

    void deleteCart(Long id);
}
