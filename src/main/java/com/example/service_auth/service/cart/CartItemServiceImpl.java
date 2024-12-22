package com.example.service_auth.service.cart;

import com.example.service_auth.dto.request.CartItemRequest;
import com.example.service_auth.dto.request.UpdateCartItemRequest;
import com.example.service_auth.dto.response.CartItemResponse;
import com.example.service_auth.dto.response.ProductResponse;
import com.example.service_auth.entity.*;
import com.example.service_auth.exception.AppException;
import com.example.service_auth.exception.ErrorCode;
import com.example.service_auth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService{

    CartRepository cartRepository;

    UserRepository userRepository;

    CartItemRepository cartItemRepository;

    ProductRepository productRepository;
    CouponRepository couponRepository;
    @Override
    @Transactional
    public CartItemResponse addToCartItem(CartItemRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userName == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsername(userName).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy giỏ hàng của user hoặc tạo mới nếu chưa có
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setTotalPrice(0L);
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });


        // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setProductQuantity(cartItem.getProductQuantity() + 1);
            cartItem.setTotalPrice(cartItem.getProductPrice() * cartItem.getProductQuantity());
        } else {
            cartItem = new CartItem();

            Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_EXISTED));
            if(product.getCoupon() == null){
                cartItem.setDiscountPercentage(0);
            }
            else{
                Coupon coupon = couponRepository.findById(product.getCoupon().getId()).orElseThrow(() -> new AppException(ErrorCode.COUPON_EXISTED));
                cartItem.setDiscountPercentage(coupon.getDiscountPercentage());
            }
            cartItem.setProductId(product.getId());
            cartItem.setProductName(product.getName());
            cartItem.setProductQuantity(1);
            cartItem.setProductPrice(product.getPrice());
            cartItem.setTotalPrice(cartItem.getProductPrice() * cartItem.getProductQuantity());
            cartItem.setCart(cart);
        }
        cartItemRepository.save(cartItem);

        // Cập nhật tổng giá của giỏ hàng
        long totalPrice = cartItemRepository.findAllByCartId(cart.getId())
                .stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .discountPercentage(cartItem.getDiscountPercentage())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .productQuantity(cartItem.getProductQuantity())
                .productPrice(cartItem.getProductPrice())
                .totalPrice(cartItem.getTotalPrice())
                .cartId(cartItem.getCart().getId())
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public void deleteCartItem(UpdateCartItemRequest request) {

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(()->new AppException(ErrorCode.EMPTY_ARRAY));

        cartItemRepository.deleteById(request.getCartItemId());



        long totalPriceCart = cartItemRepository.findAllByCartId(cartItem.getCart().getId())
                .stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();

        Cart cart = cartRepository.findById(cartItem.getCart().getId()).orElseThrow(()->new AppException(ErrorCode.EMPTY_ARRAY));
        cart.setTotalPrice(totalPriceCart);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(UpdateCartItemRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userName == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsername(userName).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));


        CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(()->new AppException(ErrorCode.EMPTY_ARRAY));
        cartItem.setProductQuantity(request.getQuantity());
        cartItem.setTotalPrice(cartItem.getProductPrice() * request.getQuantity());
        cartItemRepository.save(cartItem);


        long totalPriceCart = cartItemRepository.findAllByCartId(cartItem.getCart().getId())
                .stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();

        Cart cart = cartRepository.findById(cartItem.getCart().getId()).orElseThrow(()->new AppException(ErrorCode.EMPTY_ARRAY));
        cart.setTotalPrice(totalPriceCart);
        cartRepository.save(cart);


        return CartItemResponse.builder()
                .id(cartItem.getId())
                .discountPercentage(cartItem.getDiscountPercentage())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .productQuantity(cartItem.getProductQuantity())
                .productPrice(cartItem.getProductPrice())
                .totalPrice(cartItem.getTotalPrice())
                .cartId(cartItem.getCart().getId())
                .userId(user.getId())
                .build();
    }
}
