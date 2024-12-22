package com.example.service_auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    long discountPercentage;
    String productId;
    String productName;
    int productQuantity;
    long productPrice;
    long totalPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id" , nullable = false)
    Cart cart;



}
