package com.dapa.dapa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryName(String categoryName);
}
