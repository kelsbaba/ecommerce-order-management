package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem getCartItemById(Long id) {
      return cartItemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart item not found")
        );
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem updateCartItem(Long id, CartItem cartItem) {
       CartItem existingCartItem =  cartItemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart item not found"));

        existingCartItem.setCart(cartItem.getCart());
        existingCartItem.setProduct(cartItem.getProduct());
        existingCartItem.setQuantity(cartItem.getQuantity());

       return cartItemRepository.save(existingCartItem);
    }

    @Override
    public void deleteCartItem(Long id) {
      CartItem existingCartItem =  cartItemRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Cart item not found"));

      cartItemRepository.delete(existingCartItem);

    }
}
