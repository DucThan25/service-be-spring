package com.example.service_auth.service;

import com.example.service_auth.dto.request.CategoryRequest;
import com.example.service_auth.entity.Category;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.CategoryRepository;
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
public class CategoryService {
    CategoryRepository categoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory (CategoryRequest request){
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setActive(true);
        category.setDelete(false);

        categoryRepository.save(category);
        return category;
    }

    public List<Category> getAllBrand(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return categories;
    }

    public Category getByID(String id ) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return category;
    }

    public List<Category> getByIsActive() {
        List<Category> categories = categoryRepository.findAllActiveCategory();
        if (categories.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return categories;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory (String id , CategoryRequest request){
        Category category = getByID(id);
        category.setName(request.getName());
        categoryRepository.save(category);
        return category;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void updateIsActive (String id) {
        Category category = getByID(id);
        category.setActive(!category.isActive());
        categoryRepository.save(category);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Category softDelete (String id){
        Category category = getByID(id);
        category.setDelete(true);
        categoryRepository.save(category);
        return category;
    }
}
