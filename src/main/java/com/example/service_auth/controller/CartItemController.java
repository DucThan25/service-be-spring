package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.CartItemRequest;
import com.example.service_auth.dto.request.UpdateCartItemRequest;
import com.example.service_auth.dto.response.CartItemResponse;
import com.example.service_auth.service.cart.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-item")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
public class CartItemController {
    CartItemService cartItemService;

    @PostMapping("/addtocart")
    public ApiResponse<CartItemResponse> addToCart (@RequestBody CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Thêm thành công sản phẩm vào giỏ hàng")
                .result(cartItemService.addToCartItem(request))
                .build();
    }

    @PutMapping ("/update")
    public ApiResponse<CartItemResponse> updateCart (@RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Cập nhật thành công")
                .result(cartItemService.updateCartItem(request))
                .build();
    }

    @DeleteMapping ("/delete")
    public ApiResponse deleteCart (@RequestBody UpdateCartItemRequest request) {
        cartItemService.deleteCartItem(request);
        return ApiResponse.builder()
                .code(200)
                .message("Xóa thành công ")
                .build();
    }

}
