package com.leonmesquita.ecommerce.payment_microservice.controllers;

import com.leonmesquita.ecommerce.payment_microservice.dtos.PaymentRequestDTO;
import com.leonmesquita.ecommerce.payment_microservice.enums.PaymentStatusEnum;
import com.leonmesquita.ecommerce.payment_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.payment_microservice.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<String> payOrder(@RequestBody @Valid PaymentRequestDTO body) {
        return ResponseEntity.ok(paymentService.payOrder(body));
    }


}
