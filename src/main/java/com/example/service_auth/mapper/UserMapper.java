package com.example.service_auth.mapper;

import com.example.service_auth.dto.request.UserCreationRequest;
import com.example.service_auth.dto.request.UserUpdateRequest;
import com.example.service_auth.dto.response.UserResponse;
import com.example.service_auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    User toUser(UserCreationRequest request);
    @Mapping(target = "lastName", ignore = false)
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
