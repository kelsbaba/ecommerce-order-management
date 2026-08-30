package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);

    Cart getCartById(Long id);

    Cart getCartByUserId(Long userid);

    List<Cart> getAllCarts();

    Cart updateCart(Long id, Cart cart);

    void deleteCart(Long id);

    CartItem addProductToCart(Long cartId, Long productId, int quantity);

    CartItem decreaseProductQuantity(Long cartId, Long productId, int quantity);

}
