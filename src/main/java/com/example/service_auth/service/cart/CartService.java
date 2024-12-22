package com.example.service_auth.service.cart;

import com.example.service_auth.entity.CartItem;

import java.util.List;

public interface CartService {
    List<CartItem> getCartByUserId();
}
