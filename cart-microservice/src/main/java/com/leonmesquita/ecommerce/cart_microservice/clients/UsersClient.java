package com.leonmesquita.ecommerce.cart_microservice.clients;

import com.leonmesquita.ecommerce.cart_microservice.dtos.feign.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-microservice", path = "/auth")
public interface UsersClient {
    @GetMapping("/user/{email}")
    ResponseEntity<UserResponseDTO> getUserByEmail(@RequestParam String email);
}
