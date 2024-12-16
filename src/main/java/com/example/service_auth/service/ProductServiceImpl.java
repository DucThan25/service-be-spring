package com.example.service_auth.service;

import com.example.service_auth.dto.request.ProductRequest;
import com.example.service_auth.dto.response.PageResponse;
import com.example.service_auth.dto.response.ProductResponse;
import com.example.service_auth.entity.Brand;
import com.example.service_auth.entity.Category;
import com.example.service_auth.entity.Coupon;
import com.example.service_auth.entity.Product;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.BrandRepository;
import com.example.service_auth.repository.CategoryRepository;
import com.example.service_auth.repository.CouponRepository;
import com.example.service_auth.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements  ProductService{
    BrandRepository brandRepository;
    CategoryRepository categoryRepository;
    CouponRepository couponRepository;
    ProductRepository productRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Brand brand =  brandRepository.findById(request.getBrandId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
        product.setBrand(brand);
        Category category = categoryRepository.findById(request.getCategoryID()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
        product.setCategory(category);
        product.setActive(true);

        if(request.getCouponID() == null){
            product.setCoupon(null);
        }
        else{
           Coupon coupon = couponRepository.findById(request.getCouponID()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
           product.setCoupon(coupon);
        }
        productRepository.save(product);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setShortDescription(product.getShortDescription());
        productResponse.setBrandName(brand.getName());
        productResponse.setCategoryName(category.getName());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setBrandId(brand.getId());
        productResponse.setCategoryId(category.getId());
        if (product.getCoupon()== null){
            return productResponse;
        }
        productResponse.setCouponId(product.getCoupon().getId());
        productResponse.setCouponName(product.getCoupon().getName());
        productResponse.setCouponDiscount(product.getCoupon().getDiscountPercentage());
        return productResponse;
    }

    @Override
    public PageResponse<ProductResponse> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        var pageResponse = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = pageResponse.getContent().stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setId(product.getId());
                    productResponse.setName(product.getName());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setShortDescription(product.getShortDescription());
                    Brand brand =  brandRepository.findById(product.getBrand().getId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
                    Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
                    productResponse.setBrandName(brand.getName());
                    productResponse.setCategoryName(category.getName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setBrandId(brand.getId());
                    productResponse.setCategoryId(category.getId());
                    if (product.getCoupon()== null){
                        return productResponse;
                    }
                    productResponse.setCouponId(product.getCoupon().getId());
                    productResponse.setCouponName(product.getCoupon().getName());
                    productResponse.setCouponDiscount(product.getCoupon().getDiscountPercentage());
                    return productResponse;
                })
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .currentPage(page + 1)
                .pageSize(size)
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(productResponses)
                .build();
    }

    @Override
    public PageResponse<ProductResponse> getAllProductByBrand(String brandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());

        Brand brandID =  brandRepository.findById(brandId).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
        var pageResponse = productRepository.findAllByBrandId(brandID.getId(), pageable);

        List<ProductResponse> productResponses = pageResponse.getContent().stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setId(product.getId());
                    productResponse.setName(product.getName());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setShortDescription(product.getShortDescription());
                    Brand brand =  brandRepository.findById(product.getBrand().getId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
                    Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
                    productResponse.setBrandName(brand.getName());
                    productResponse.setCategoryName(category.getName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setBrandId(brand.getId());
                    productResponse.setCategoryId(category.getId());
                    if (product.getCoupon()== null){
                        return productResponse;
                    }
                    productResponse.setCouponId(product.getCoupon().getId());
                    productResponse.setCouponName(product.getCoupon().getName());
                    productResponse.setCouponDiscount(product.getCoupon().getDiscountPercentage());
                    return productResponse;
                })
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .currentPage(page + 1)
                .pageSize(size)
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(productResponses)
                .build();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Brand brand =  brandRepository.findById(request.getBrandId()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
        product.setBrand(brand);
        Category category = categoryRepository.findById(request.getCategoryID()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
        product.setCategory(category);
        if(request.getCouponID() == null){
            product.setCoupon(null);
        }
        else{
            Coupon coupon = couponRepository.findById(request.getCouponID()).orElseThrow(() -> new AppException(ErrorCode.EMPTY_ARRAY));
            product.setCoupon(coupon);
        }

        productRepository.save(product);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setShortDescription(product.getShortDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setBrandName(brand.getName());
        productResponse.setCategoryName(category.getName());
        productResponse.setBrandId(brand.getId());
        productResponse.setCategoryId(category.getId());
        if (product.getCoupon()== null){
            return productResponse;
        }
        productResponse.setCouponId(product.getCoupon().getId());
        productResponse.setCouponName(product.getCoupon().getName());
        productResponse.setCouponDiscount(product.getCoupon().getDiscountPercentage());
        return productResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateProductIsActive (String id) {
        Product product = getProductById(id);
        product.setActive(!product.isActive());
        productRepository.save(product);
    }


}
