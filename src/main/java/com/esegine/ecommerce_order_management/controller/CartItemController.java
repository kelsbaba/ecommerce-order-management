package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.CartItem;
import com.esegine.ecommerce_order_management.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public CartItem createCartItem(@Valid @RequestBody CartItem cartItem) {
        return cartItemService.createCartItem(cartItem);
    }

    @GetMapping("/{id}")
    public CartItem getCartItem(@PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    @PutMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Long id,
                                   @Valid @RequestBody CartItem cartItem) {
       return cartItemService.updateCartItem(id, cartItem);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
}
