package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.ProductRequest;
import com.example.service_auth.dto.response.PageResponse;
import com.example.service_auth.dto.response.ProductResponse;
import com.example.service_auth.entity.Product;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/product")
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request){
        return  ApiResponse.<ProductResponse>builder()
                .code(200)
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/product")
    ApiResponse<PageResponse> getALl(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value = "size", required = false, defaultValue = "10") int size){
        if(page < 1 || size < 1){
            throw new AppException(ErrorCode.EMPTY_ARRAY);
        }
        return ApiResponse.<PageResponse>builder()
                .code(200)
                .result(productService.getAllProduct(page -1 , size))
                .build();
    }

    @GetMapping("/product/brand")
    ApiResponse<PageResponse> getProductBranID(
            @RequestParam(value = "brandId", required = true) String brandId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value = "size", required = false, defaultValue = "10") int size){
        if(page < 1 || size < 1){
            throw new AppException(ErrorCode.EMPTY_ARRAY);
        }
        return ApiResponse.<PageResponse>builder()
                .code(200)
                .result(productService.getAllProductByBrand(brandId,page -1 , size))
                .build();
    }

    @GetMapping("/product/{id}")
    ApiResponse<Product> getProductById(@PathVariable("id") String id){
        return ApiResponse.<Product>builder()
                .code(200)
                .result(productService.getProductById(id))
                .build();
    }

    @PutMapping("/product/{id}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable("id") String id, @RequestBody ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .result(productService.updateProduct(id, request))
                .build();
    }

    @PutMapping("/product/active/{id}")
    ApiResponse<Void> updateProductActive(@PathVariable("id") String id){
        productService.updateProductIsActive(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("update status active sucsessfuly.")
                .build();
    }
}
