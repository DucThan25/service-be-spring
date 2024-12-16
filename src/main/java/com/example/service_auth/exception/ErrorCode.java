package com.example.service_auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST ),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    EMPTY_ARRAY(1000, "Empty array", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "you do not permission", HttpStatus.FORBIDDEN),
    PERMISSION_NOT_FOUND(1008, "Permission does not exist", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1009, "Permission existed", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1010, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    BRAND_NAME_INVALID(1011, "Brandname must be at least {min} characters", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED(1012, "Brand name existed" , HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_INVALID(1011, "Categoryname must be at least {min} characters", HttpStatus.BAD_REQUEST),
    COUPON_NAME_INVALID(1013, "Coupon name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    COUPON_EXISTED(1012, "Coupon name existed" , HttpStatus.BAD_REQUEST),
    MAX_DISCOUNT(1012, "max_discount is 100", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1013, "Product name existed" , HttpStatus.BAD_REQUEST),
    ;


    ErrorCode(int code, String message , HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
