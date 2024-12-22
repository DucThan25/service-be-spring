package com.example.service_auth.repository;

import com.example.service_auth.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    // Lấy danh sách CartItem theo cartId
    List<CartItem> findAllByCartId(String cartId);
    // Tìm CartItem theo cartId và productId
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);

    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.id = :userId")
    List<CartItem> findAllByUserId(String userId);
}
