package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.skillcert.backend.entity.CategoryStatus;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByOrderByOrderIndexAsc();
    
    List<Category> findByStatusOrderByOrderIndexAsc(CategoryStatus status);
    
    @Query("SELECT c FROM Category c WHERE c.prerequisiteCategory.id = :categoryId")
    List<Category> findDependentCategories(@Param("categoryId") Long categoryId);
    
    boolean existsByName(String name);
}