package com.example.service_auth.dto.response;

import com.example.service_auth.entity.Brand;
import com.example.service_auth.entity.Category;
import com.example.service_auth.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String description;
    String shortDescription;
    long price;
    int quantity;
    String brandId;
    String brandName;
    String categoryId;
    String categoryName;
    String couponId;
    String couponName;
    long couponDiscount;
}
