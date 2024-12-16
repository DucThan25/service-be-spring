package com.example.service_auth.service;

import com.example.service_auth.dto.request.RoleRequest;
import com.example.service_auth.dto.response.RoleResponse;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.mapper.RoleMapper;
import com.example.service_auth.repository.PermissionRepository;
import com.example.service_auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create (RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        List<RoleResponse> roles = roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
        if (roles.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return roles;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
