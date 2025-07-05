package com.leonmesquita.ecommerce.cart_microservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @JsonIgnore
    private CartModel cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private BigDecimal unitPrice;
}
