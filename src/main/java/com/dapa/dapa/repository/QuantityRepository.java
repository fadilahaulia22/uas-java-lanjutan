package com.dapa.dapa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.Quantity;
import com.dapa.dapa.entity.Users;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuantityRepository extends JpaRepository<Quantity, String> {
    List<Quantity> findByUsers(Users users);

    Quantity findByUsersAndProducts(Users users, Products products);

    List<Quantity> findQuantityByUsersAndProducts(Users users, Products products);

    @Transactional
    void deleteByProductsAndUsers(Products products, Users users);
}
