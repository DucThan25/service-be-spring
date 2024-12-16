package com.example.service_auth.dto.request;

import com.example.service_auth.entity.Brand;
import com.example.service_auth.entity.Category;
import com.example.service_auth.entity.Coupon;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    String shortDescription;
    @NotNull
    long price;
    @NotNull
    int quantity;

    @NotNull
    String brandId;
    @NotNull
    String categoryID;
    String couponID;
}
