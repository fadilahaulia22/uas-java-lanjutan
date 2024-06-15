package com.dapa.dapa.controller.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.entity.Transaction;
import com.dapa.dapa.service.transaction.TransactionService;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transaction")
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/post")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> pay(){
        try {
                return ResponseEntity.ok().body(GenericResponse.success(transactionService.addTransaction(), "Transactio Successfully"));
        } catch (Exception e) {
            log.error("Error occurred while processing transaction", e);
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public List<Transaction> findAll(){
        return transactionService.getAllTransactions();
    }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> remove(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(transactionService.deleteTransaction(id), "Successfully delete transaction"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
