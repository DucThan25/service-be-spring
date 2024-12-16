package com.example.service_auth.repository;

import com.example.service_auth.entity.Brand;
import com.example.service_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {

    boolean existsByName(String name);
    @Query("SELECT b FROM Brand b WHERE b.isActive = true AND b.isDelete = false")
    List<Brand> findAllActiveBrands();
}
