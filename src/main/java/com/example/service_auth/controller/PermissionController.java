package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.PermissionRequest;
import com.example.service_auth.dto.response.PermissionResponse;
import com.example.service_auth.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionResponse> create (@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .code(200)
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .result(permissionService.getAll())
                .build();
    }


    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Permission deleted")
                .build();
    }

}
