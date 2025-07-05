package com.leonmesquita.ecommerce.order_microservice.clients;

import com.leonmesquita.ecommerce.order_microservice.dtos.feign.CartResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cart-microservice", path = "/carts")
public interface CartsClient {
    @GetMapping("/{id}")
    ResponseEntity<CartResponseDTO> getCartById(@PathVariable Long id);
}
