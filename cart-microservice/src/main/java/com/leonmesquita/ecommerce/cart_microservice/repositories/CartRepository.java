package com.leonmesquita.ecommerce.cart_microservice.repositories;

import com.leonmesquita.ecommerce.cart_microservice.models.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {
}
