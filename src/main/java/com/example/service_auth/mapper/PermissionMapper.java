package com.example.service_auth.mapper;

import com.example.service_auth.dto.request.PermissionRequest;
import com.example.service_auth.dto.response.PermissionResponse;
import com.example.service_auth.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
