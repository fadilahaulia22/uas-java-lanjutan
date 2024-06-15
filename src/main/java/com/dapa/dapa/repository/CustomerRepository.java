package com.dapa.dapa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,String>{
    
}
