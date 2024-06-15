package com.dapa.dapa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.SumTransaction;
import com.dapa.dapa.entity.Transaction;

import jakarta.transaction.Transactional;

public interface SumTransactionRepository extends JpaRepository<SumTransaction,String>{
    SumTransaction findByTransactionAndProducts(Transaction transaction,Products products) ;
    @Transactional
    void deleteAllByTransaction(Transaction transaction);
}
