package com.example.service_auth.mapper;

import com.example.service_auth.dto.request.RoleRequest;
import com.example.service_auth.dto.response.RoleResponse;
import com.example.service_auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request); //converts tu RoleRequest ve  Role
    RoleResponse toRoleResponse(Role role);
}
