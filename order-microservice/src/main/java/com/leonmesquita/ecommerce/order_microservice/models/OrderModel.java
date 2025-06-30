package com.leonmesquita.ecommerce.order_microservice.models;

import com.leonmesquita.ecommerce.order_microservice.models.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private List<OrderItemModel> items;

    @Column
    private BigDecimal total;

    @Column
    private OrderStatusEnum status;

    @Column
    private LocalDateTime creationDate;

}
