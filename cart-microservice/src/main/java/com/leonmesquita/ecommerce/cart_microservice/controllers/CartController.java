package com.leonmesquita.ecommerce.cart_microservice.controllers;

import com.leonmesquita.ecommerce.cart_microservice.dtos.CartItemDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.UserResponseDTO;
import com.leonmesquita.ecommerce.cart_microservice.models.CartModel;
import com.leonmesquita.ecommerce.cart_microservice.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    CartService cartService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CartModel> addItemToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CartItemDTO body) {
        String userEmail = jwt.getSubject();
        return ResponseEntity.status(HttpStatus.OK).body(cartService.addItemsToCart(userEmail, body));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CartModel> getUserCart(@AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getSubject();
        return ResponseEntity.status(HttpStatus.OK).body(cartService.findByUser(userEmail));
    }
}
