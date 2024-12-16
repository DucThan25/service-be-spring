package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.CouponRequest;
import com.example.service_auth.entity.Coupon;
import com.example.service_auth.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class CouponController {
    CouponService couponService;

    @PostMapping("/coupon")
    ApiResponse<Coupon> createCoupon(@RequestBody @Valid CouponRequest request) {
        return ApiResponse.<Coupon>builder()
                .code(200)
                .result(couponService.createCoupon(request))
                .build();
    }

    @GetMapping("/coupons")
    ApiResponse<List<Coupon>> getAll(){
        ApiResponse<List<Coupon>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(couponService.getAllCoupon());
        return apiResponse;
    }

    @PutMapping("/coupon/{id}")
    ApiResponse<Coupon>  updateUser(@PathVariable("id") String id, @RequestBody @Valid CouponRequest request){
        return ApiResponse.<Coupon>builder()
                .code(200)
                .result(couponService.updateCoupon(id, request))
                .build();
    }




}
