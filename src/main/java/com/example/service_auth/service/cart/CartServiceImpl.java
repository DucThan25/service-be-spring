package com.example.service_auth.service.cart;

import com.example.service_auth.entity.CartItem;
import com.example.service_auth.entity.User;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.CartItemRepository;
import com.example.service_auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<CartItem> getCartByUserId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userName == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsername(userName).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return cartItemRepository.findAllByUserId(user.getId());
    }
}
