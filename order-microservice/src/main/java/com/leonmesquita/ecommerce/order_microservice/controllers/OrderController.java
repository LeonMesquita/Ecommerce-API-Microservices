package com.leonmesquita.ecommerce.order_microservice.controllers;

import com.leonmesquita.ecommerce.order_microservice.dtos.OrderDTO;
import com.leonmesquita.ecommerce.order_microservice.models.OrderModel;
import com.leonmesquita.ecommerce.order_microservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody @Valid OrderDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(body));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderModel>> getAllUserOrders(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllUserOrders(userId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<OrderModel> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getOrderById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findById(id));
    }
}
