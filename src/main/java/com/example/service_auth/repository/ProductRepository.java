package com.example.service_auth.repository;

import com.example.service_auth.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId")
    Page<Product> findAllByBrandId(String brandId, Pageable pageable);
}
