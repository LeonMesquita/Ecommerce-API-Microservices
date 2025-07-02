package com.leonmesquita.ecommerce.cart_microservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart")
//    @JsonIgnore
    private List<CartItemModel> items;

    @Column
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    private boolean active = true;

}
