package com.example.service_auth.service;

import com.example.service_auth.constant.PredefinedRole;
import com.example.service_auth.dto.request.UserCreationRequest;
import com.example.service_auth.dto.request.UserUpdateRequest;
import com.example.service_auth.dto.response.UserResponse;
import com.example.service_auth.entity.User;
import com.example.service_auth.enums.Role;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.mapper.UserMapper;
import com.example.service_auth.repository.RoleRepository;
import com.example.service_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor  // tao constructor voi tham so la cac bean duoc inject vao thay the cho @Autowired nhuwng phai final
@FieldDefaults(level = lombok.AccessLevel.PRIVATE , makeFinal = true)  // makeFinal = true tao ra cac bien final auto
public class UserService {
    final UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    public User createUser(UserCreationRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

//        //cach1
        User user = new User();

        //set cac gia tri cho user tu UserCreationRequest
        user.setUsername(request.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        // cach 2 su dung mapstruct
//        User user = userMapper.toUser(request);
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(PredefinedRole.USER_ROLE);
        log.info("roles: {}", roles.stream().toList());
        user.setRoles(new HashSet<>(roleRepository.findAllById(roles)));
        //user.setRoles(roles);

        return userRepository.save(user);
    }

    public UserResponse getMyInfo() {
        //Khi ma da xac thuc thanh cong thi se co thong tin cua user duoc luu trong securityContextHolder
        var context = SecurityContextHolder.getContext();
        log.warn("context: {}", context);
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        //return userRepository.findAll();
        List<UserResponse> users = userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
        if (users.isEmpty()) {
            throw new AppException((ErrorCode.EMPTY_ARRAY));
        }
        return users;
    }

//    public User getUser(String id) {
//        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
    public UserResponse updateUser(String id, UserUpdateRequest request) {

        //User user = getUser(id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        userMapper.updateUser(user, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
