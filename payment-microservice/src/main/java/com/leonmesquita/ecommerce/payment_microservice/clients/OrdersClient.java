package com.leonmesquita.ecommerce.payment_microservice.clients;

import com.leonmesquita.ecommerce.payment_microservice.dtos.feign.OrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-microservice", path = "/orders")
public interface OrdersClient {
    @GetMapping("/{id}")
    ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id);
}
