package com.leonmesquita.ecommerce.product_microservice.repositories;

import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    Optional<ProductModel> findByName(String name);

    boolean existsByName(String name);
}
