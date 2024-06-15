package com.dapa.dapa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dapa.dapa.entity.Cart;
import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.Users;

public interface CartRepository extends JpaRepository<Cart,String>{
    Cart findByUsers(Users users);
    Cart findQuantityByUsersAndProducts(Users user,Products product);
    Optional<Cart> findById(String id);
    @Transactional
    void deleteAllByUsers(Users users);
}
