package com.leonmesquita.ecommerce.product_microservice.controllers;

import com.leonmesquita.ecommerce.product_microservice.dtos.ProductDTO;
import com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq.OrderItem;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductModel>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<ProductModel> products = productService.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(body));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO body) {
        return ResponseEntity.ok(productService.update(id, body));
    }

//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @PutMapping("/stock")
//    public ResponseEntity<Void> updateProductStock(@RequestBody @Valid List<OrderItem> body) {
//        productService.updateStock(body);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/stock")
    public ResponseEntity<Void> checkProductStock(@RequestBody @Valid List<OrderItem> body) {
        productService.checkStockAvailable(body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
