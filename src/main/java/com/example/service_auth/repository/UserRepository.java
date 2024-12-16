package com.example.service_auth.repository;

import com.example.service_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username); //check xem username da ton tai chua

    Optional <User> findByUsername(String username); //tim user theo username
}
