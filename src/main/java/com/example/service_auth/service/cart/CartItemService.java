package com.example.service_auth.service.cart;

import com.example.service_auth.dto.request.CartItemRequest;
import com.example.service_auth.dto.request.UpdateCartItemRequest;
import com.example.service_auth.dto.response.CartItemResponse;

public interface CartItemService {
    CartItemResponse addToCartItem(CartItemRequest request);
    void deleteCartItem(UpdateCartItemRequest request);
    CartItemResponse updateCartItem(UpdateCartItemRequest request);
}
