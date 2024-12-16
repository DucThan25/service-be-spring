package com.example.service_auth.controller;

import com.example.service_auth.dto.request.*;
import com.example.service_auth.dto.response.UserResponse;
import com.example.service_auth.entity.Brand;
import com.example.service_auth.entity.User;
import com.example.service_auth.service.BrandService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class BrandController {
    BrandService brandService;

    @PostMapping("/brand")
    ApiResponse<Brand> createBrand(@RequestBody @Valid BrandRequest request) {
        ApiResponse<Brand> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(brandService.createBrand(request));
        return apiResponse;
    };

    @GetMapping("/brands")
    ApiResponse<List<Brand>> getAll(){
        ApiResponse<List<Brand>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(brandService.getAllBrand());
        return apiResponse;
    }

    @GetMapping("/brands/is-active")
    ApiResponse<List<Brand>> getAllIsActive(){
        ApiResponse<List<Brand>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(brandService.getByIsActive());
        return apiResponse;
    }

    @PutMapping("/brand/{id}")
    ApiResponse<Brand>  updateUser(@PathVariable("id") String id, @RequestBody @Valid BrandRequest request){
        return ApiResponse.<Brand>builder()
                .code(200)
                .result(brandService.updateBrand(id, request))
                .build();
    }

    @PutMapping("brand/is-active/{id}")
    ApiResponse<Void> isActive(@PathVariable("id") String id) {
        brandService.updateIsActive(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("update status active sucsessfuly.")
                .build();
    }

    @PutMapping("brand/delete-soft/{id}")
    ApiResponse<Void> delete(@PathVariable("id") String id) {
        brandService.softDelete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete soft sucsessfuly.")
                .build();
    }

}
