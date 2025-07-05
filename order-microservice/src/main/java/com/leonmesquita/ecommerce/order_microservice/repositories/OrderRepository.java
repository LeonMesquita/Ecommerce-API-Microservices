package com.leonmesquita.ecommerce.order_microservice.repositories;

import com.leonmesquita.ecommerce.order_microservice.models.OrderModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    @EntityGraph(attributePaths = {"items"})
    List<OrderModel> findAllByUserId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"items"})
    Optional<OrderModel> findById(Long id);
}
