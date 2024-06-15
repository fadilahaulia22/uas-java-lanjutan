package com.dapa.dapa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Transaction;
import com.dapa.dapa.entity.Users;

public interface TransactionRepository extends JpaRepository<Transaction,String>{
    List<Transaction> findTransactionByUsers(Users users);

    Transaction findByUsers(Users users);

}
