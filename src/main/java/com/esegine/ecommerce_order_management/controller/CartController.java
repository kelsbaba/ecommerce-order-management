package com.esegine.ecommerce_order_management.controller;

import com.esegine.ecommerce_order_management.entity.Cart;
import com.esegine.ecommerce_order_management.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartservice;

    public CartController(CartService cartservice) {
        this.cartservice = cartservice;
    }

    @PostMapping
    public Cart createCart(@Valid @RequestBody Cart cart) {
        return cartservice.createCart(cart);
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartservice.getCartById(id);
    }

    @GetMapping("/user/{userid}")
    public Cart getCartByUserId(@PathVariable Long userId) {
        return cartservice.getCartByUserId(userId);
    }

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartservice.getAllCarts();
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable Long id, @Valid @RequestBody Cart cart) {
        return cartservice.updateCart(id, cart);
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartservice.deleteCart(id);
    }
}
