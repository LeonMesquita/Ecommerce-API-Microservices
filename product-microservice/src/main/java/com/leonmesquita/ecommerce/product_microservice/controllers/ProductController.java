package com.leonmesquita.ecommerce.product_microservice.controllers;

import com.leonmesquita.ecommerce.product_microservice.dtos.ProductDTO;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<String> getProducts() {
        return ResponseEntity.ok("Produtos retornados");
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(body));
    }
}
