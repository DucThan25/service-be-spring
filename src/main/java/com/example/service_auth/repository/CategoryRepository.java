package com.example.service_auth.repository;

import com.example.service_auth.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByName(String name);
    @Query("SELECT b FROM Category b WHERE b.isActive = true AND b.isDelete = false")
    List<Category> findAllActiveCategory();
}
