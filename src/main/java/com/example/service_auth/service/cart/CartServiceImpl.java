package com.example.service_auth.service.cart;

import com.example.service_auth.dto.response.CartItemResponse;
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
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<CartItemResponse> getCartByUserId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userName == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsername(userName).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        var cartItems = cartItemRepository.findAllByUserId(user.getId());


        List<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(cartItem -> {
                    CartItemResponse cartItemResponse = new CartItemResponse();
                    cartItemResponse.setId(cartItem.getId());
                    cartItemResponse.setCartId(cartItem.getCart().getId());
                    cartItemResponse.setProductId(cartItem.getProductId());
                    cartItemResponse.setProductName(cartItem.getProductName());
                    cartItemResponse.setProductPrice(cartItem.getProductPrice());
                    cartItemResponse.setProductQuantity(cartItem.getProductQuantity());
                    cartItemResponse.setDiscountPercentage(cartItem.getDiscountPercentage());
                    cartItemResponse.setTotalPrice(cartItem.getTotalPrice());
                    cartItemResponse.setUserId(cartItem.getCart().getUser().getId());
                    return cartItemResponse;
                })
                .collect(Collectors.toList());
        return cartItemResponses;
    }
}
