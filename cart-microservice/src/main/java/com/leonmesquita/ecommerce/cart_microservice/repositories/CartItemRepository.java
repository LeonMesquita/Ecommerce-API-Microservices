package com.leonmesquita.ecommerce.cart_microservice.repositories;

import com.leonmesquita.ecommerce.cart_microservice.models.CartItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemModel, Long> {
}
