package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.entity.CartItem;
import com.example.service_auth.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
public class CartController {
    CartService cartService;

    // xem chi tiết cart (xem  danh sách các item trong cart)
    @GetMapping("/get-user")
    public ApiResponse<List<CartItem>> getCartById () {
        return ApiResponse.<List<CartItem>>builder()
                .code(200)
                .message("success")
                .result(cartService.getCartByUserId())
                .build();
    }
}
