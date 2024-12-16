package com.example.service_auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRequest {
    @Size(min = 3, message = "COUPON_NAME_INVALID")
    String name;
    @Max(value = 99 , message = "MAX_DISCOUNT")
    long discountPercentage;
}
