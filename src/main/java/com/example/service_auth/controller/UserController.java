package com.example.service_auth.controller;

import com.example.service_auth.dto.request.ApiResponse;
import com.example.service_auth.dto.request.UserCreationRequest;
import com.example.service_auth.dto.request.UserUpdateRequest;
import com.example.service_auth.dto.response.UserResponse;
import com.example.service_auth.entity.User;
import com.example.service_auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

//    @PostMapping("/users")
//    User createUser(@RequestBody @Valid UserCreationRequest request) {
//        return userService.createUser(request);
//    };
    @PostMapping("/users")
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    };

    @GetMapping("/users/my-info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getMyInfo())
                .build();
    }

//    @GetMapping("/users")
//    List<User> getUsers(){
//        return userService.getUsers();
//    }

    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {}", authentication.getName());
        log.info("roles: {}", authentication.getAuthorities().stream().map(Object::toString).toList());
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/users/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/users/{userId}")
    UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/users/{userId}")
    String deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return "Delete user successfully";
    }
}
