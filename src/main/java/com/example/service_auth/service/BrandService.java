package com.example.service_auth.service;

import com.example.service_auth.dto.request.BrandRequest;
import com.example.service_auth.entity.Brand;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Brand createBrand (BrandRequest request){
        if (brandRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setActive(true);
        brand.setDelete(false);

        brandRepository.save(brand);
        return brand;
    }

    public List<Brand> getAllBrand(){
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return brands;
    }

    public Brand getByID(String id ) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return brand;
    }

    public List<Brand> getByIsActive() {
        List<Brand> brands = brandRepository.findAllActiveBrands();
        if (brands.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return brands;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Brand updateBrand (String id ,BrandRequest request){
        Brand brand = getByID(id);
        brand.setName(request.getName());
        brandRepository.save(brand);
        return brand;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void updateIsActive (String id) {
        Brand brand = getByID(id);
        brand.setActive(!brand.isActive());
        brandRepository.save(brand);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Brand softDelete (String id){
        Brand brand = getByID(id);
        brand.setDelete(true);
        brandRepository.save(brand);
        return brand;
    }
}
