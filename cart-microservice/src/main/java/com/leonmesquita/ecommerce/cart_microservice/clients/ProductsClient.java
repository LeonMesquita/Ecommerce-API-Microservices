package com.leonmesquita.ecommerce.cart_microservice.clients;

import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-microservice", path = "/products")
public interface ProductsClient {

    @GetMapping("/{id}")
    ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id);
}
