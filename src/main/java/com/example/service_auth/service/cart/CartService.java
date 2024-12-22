package com.example.service_auth.service.cart;

import com.example.service_auth.dto.response.CartItemResponse;
import com.example.service_auth.entity.CartItem;

import java.util.List;

public interface CartService {
    List<CartItemResponse> getCartByUserId();
}
