package com.leonmesquita.ecommerce.order_microservice.clients;

import com.leonmesquita.ecommerce.order_microservice.dtos.feign.CartItemDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-microservice", path = "/products")
public interface ProductsClient {
    @PutMapping("/stock")
    void checkProductStock(@RequestBody @Valid List<CartItemDTO> body);
}
