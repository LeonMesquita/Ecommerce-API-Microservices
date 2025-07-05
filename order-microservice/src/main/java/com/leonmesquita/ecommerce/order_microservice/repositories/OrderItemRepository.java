package com.leonmesquita.ecommerce.order_microservice.repositories;

import com.leonmesquita.ecommerce.order_microservice.models.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
