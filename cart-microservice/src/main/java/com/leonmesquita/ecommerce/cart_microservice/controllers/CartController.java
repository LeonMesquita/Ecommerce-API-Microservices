package com.leonmesquita.ecommerce.cart_microservice.controllers;

import com.leonmesquita.ecommerce.cart_microservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping
    public ResponseEntity<String> addItemToCart() {
        return ResponseEntity.ok("TESTE");
    }
}
