package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.CategoryRequest;
import com.example.service_auth.entity.Category;
import com.example.service_auth.service.CategoryService;
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
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/category")
    ApiResponse<Category> createBrand(@RequestBody @Valid CategoryRequest request) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(categoryService.createCategory(request));
        return apiResponse;
    };

    @GetMapping("/categorys")
    ApiResponse<List<Category>> getAll(){
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(categoryService.getAllBrand());
        return apiResponse;
    }

    @GetMapping("/categorys/is-active")
    ApiResponse<List<Category>> getAllIsActive(){
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(categoryService.getByIsActive());
        return apiResponse;
    }

    @PutMapping("/category/{id}")
    ApiResponse<Category>  updateUser(@PathVariable("id") String id, @RequestBody @Valid CategoryRequest request){
        return ApiResponse.<Category>builder()
                .code(200)
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @PutMapping("category/is-active/{id}")
    ApiResponse<Void> isActive(@PathVariable("id") String id) {
        categoryService.updateIsActive(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("update status active sucsessfuly.")
                .build();
    }

    @PutMapping("category/delete-soft/{id}")
    ApiResponse<Void> delete(@PathVariable("id") String id) {
        categoryService.softDelete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete soft sucsessfuly.")
                .build();
    }

}
