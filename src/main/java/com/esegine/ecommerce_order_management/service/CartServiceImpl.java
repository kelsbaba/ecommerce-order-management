package com.esegine.ecommerce_order_management.service;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.entity.Product;
import com.esegine.ecommerce_order_management.repository.CartItemRepository;
import com.esegine.ecommerce_order_management.repository.CartRepository;
import com.esegine.ecommerce_order_management.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public Cart getCartByUserId(Long userid) {
        return cartRepository.findByUserId(userid).orElseThrow(
                () -> new RuntimeException("Cart not found"));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart updateCart(Long id, Cart cart) {

        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        existingCart.setUser(cart.getUser());

        return cartRepository.save(existingCart);
    }

    @Override
    public void deleteCart(Long id) {

        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartRepository.delete(existingCart);
    }

    @Override
    public CartItem addProductToCart(Long cartId, Long productId, int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity most be at least 1");
        }
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new RuntimeException("Product not found"));

        if (quantity > product.getStockQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(
                cartId, productId);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();

            int newQuantity = cartItem.getQuantity() + quantity;

            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            cartItem.setQuantity(newQuantity);
            return cartItemRepository.save(cartItem);
        }
        CartItem cartItem = new CartItem(cart, product, quantity);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem decreaseProductQuantity(Long cartId, Long productId, int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        int newQuantity = cartItem.getQuantity() - quantity;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot decrease quantity below zero");
        }

        if (newQuantity == 0) {
            cartItemRepository.delete(cartItem);
            return cartItem;
        }

        cartItem.setQuantity(newQuantity);

        return cartItemRepository.save(cartItem);
    }
}