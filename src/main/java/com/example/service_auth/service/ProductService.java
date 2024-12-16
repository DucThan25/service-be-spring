package com.example.service_auth.service;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.ProductRequest;
import com.example.service_auth.dto.response.PageResponse;
import com.example.service_auth.dto.response.ProductResponse;
import com.example.service_auth.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProduct(int page, int size);

    PageResponse<ProductResponse> getAllProductByBrand(String brandId, int page, int size);

    Product getProductById(String id);

    ProductResponse updateProduct(String id, ProductRequest request);

    void updateProductIsActive(String id);
}
