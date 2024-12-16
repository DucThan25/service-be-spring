package com.example.service_auth.service;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.PermissionRequest;
import com.example.service_auth.dto.response.PermissionResponse;
import com.example.service_auth.dto.response.UserResponse;
import com.example.service_auth.entity.Permission;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.mapper.PermissionMapper;
import com.example.service_auth.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create (PermissionRequest request) {

        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<PermissionResponse> permissions = permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
        if (permissions.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return permissions;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String permission) {
        var findPermission = permissionRepository.findById(permission);
        if (findPermission.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(permission);

    }
}
