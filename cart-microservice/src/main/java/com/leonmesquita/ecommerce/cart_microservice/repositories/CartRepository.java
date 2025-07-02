package com.leonmesquita.ecommerce.cart_microservice.repositories;

import com.leonmesquita.ecommerce.cart_microservice.models.CartModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {

    @EntityGraph(attributePaths = {"items"})
    Optional<CartModel> findByUserId(Long userId);
}
