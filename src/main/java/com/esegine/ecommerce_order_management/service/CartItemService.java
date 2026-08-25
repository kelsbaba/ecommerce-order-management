package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);

    CartItem getCartItemById(Long id);

    List<CartItem> getAllCartItems();

    CartItem updateCartItem(Long id, CartItem cartItem);

    void deleteCartItem(Long id);
}
