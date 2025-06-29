package com.leonmesquita.ecommerce.product_microservice.repositories;

import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    Optional<ProductModel> findByName(String name);

    @Query("SELECT l FROM ProductModel l WHERE l.active = true")
    Page<ProductModel> listAllActiveProducts(Pageable pageable);

    boolean existsByName(String name);
}
