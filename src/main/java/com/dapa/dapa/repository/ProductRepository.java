package com.dapa.dapa.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.dapa.dapa.entity.Products;



public interface ProductRepository extends JpaRepository<Products, String> {
    Products findOneById(String id);
    Products findProductsById(String id);
}
