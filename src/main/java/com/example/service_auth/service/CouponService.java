package com.example.service_auth.service;


import com.example.service_auth.dto.request.CouponRequest;
import com.example.service_auth.entity.Coupon;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
public class CouponService {
    CouponRepository couponRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon (CouponRequest request){
        if (couponRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.COUPON_EXISTED);
        }
        Coupon coupon = new Coupon();
        coupon.setName(request.getName());
        coupon.setDiscountPercentage(request.getDiscountPercentage());

        couponRepository.save(coupon);
        return coupon;
    }

    public List<Coupon> getAllCoupon(){
        List<Coupon> coupons = couponRepository.findAll();
        if (coupons.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return coupons;
    }

    public Coupon getByID(String id ) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return coupon;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public Coupon updateCoupon (String id ,CouponRequest request){
        Coupon coupon = getByID(id);
        coupon.setName(request.getName());
        coupon.setDiscountPercentage(request.getDiscountPercentage());
        couponRepository.save(coupon);
        return coupon;
    }
}
