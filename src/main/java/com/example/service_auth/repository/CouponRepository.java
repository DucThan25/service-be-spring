package com.example.service_auth.repository;

import com.example.service_auth.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

    boolean existsByName(String name);

}
