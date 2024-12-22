package com.example.service_auth.dto.response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private String id;
    private String cartId;
    private String productId;
    private String productName;
    private long productPrice;
    private int productQuantity;
    private long discountPercentage;
    private long totalPrice;
    private String userId;
}
