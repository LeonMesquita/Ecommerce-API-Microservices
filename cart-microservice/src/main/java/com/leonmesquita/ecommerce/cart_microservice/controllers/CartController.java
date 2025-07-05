package com.leonmesquita.ecommerce.cart_microservice.controllers;

import com.leonmesquita.ecommerce.cart_microservice.dtos.CartItemDTO;
import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.CartResponseDTO;
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
        Long userId = jwt.getClaim("userId");
        return ResponseEntity.status(HttpStatus.OK).body(cartService.addItemsToCart(userId, body));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CartModel> getUserCart(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        return ResponseEntity.status(HttpStatus.OK).body(cartService.findByUser(userId));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable Long id) {
        CartModel cart = cartService.findById(id);
        CartResponseDTO dto = cartService.toResponseDTO(cart);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Long userId = jwt.getClaim("userId");
        cartService.removeItemFromCart(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
