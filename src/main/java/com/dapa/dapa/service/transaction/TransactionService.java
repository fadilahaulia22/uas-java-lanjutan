package com.dapa.dapa.service.transaction;

import java.util.List;

import com.dapa.dapa.entity.Transaction;



public interface TransactionService {
    public String addTransaction();
    public List<Transaction> getAllTransactions();
    public String deleteTransaction(String id);
}
