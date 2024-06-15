package com.dapa.dapa.service.transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dapa.dapa.entity.Cart;
import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.Quantity;
import com.dapa.dapa.entity.SumTransaction;
import com.dapa.dapa.entity.Transaction;
import com.dapa.dapa.entity.Users;
import com.dapa.dapa.repository.CartRepository;
import com.dapa.dapa.repository.ProductRepository;
import com.dapa.dapa.repository.QuantityRepository;
import com.dapa.dapa.repository.SumTransactionRepository;
import com.dapa.dapa.repository.TransactionRepository;
import com.dapa.dapa.repository.UserRepository;
import com.dapa.dapa.service.EmailService;
import com.dapa.dapa.utils.PdfUtil;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private QuantityRepository quantityRepository;

    @Autowired 
    private EmailService emailService;

    @Autowired
    private SumTransactionRepository sumTransactionRepository;

    @Override
    public String addTransaction() {
        LocalDate currentTime = LocalDate.now();
        Transaction transaction = new Transaction();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users users = userRepository.findUsersByUsername(username);
        Cart carts = cartRepository.findByUsers(users);
        transaction.getProducts().addAll(carts.getProducts());
        transaction.setTransactionDate(currentTime);
        transaction.setUsers(users);
        transactionRepository.save(transaction);
        List<Quantity> quantities = new ArrayList<>();
        List<SumTransaction> sumTransactions = new ArrayList<>();
        for(Products products : carts.getProducts()){
            SumTransaction sumTransaction = new SumTransaction();
            Quantity quantity = quantityRepository.findByUsersAndProducts(users, products);
            if(products.getProductStock() - quantity.getQuantity() >= 0){
                products.setProductStock(products.getProductStock() - quantity.getQuantity());
                sumTransaction.setProducts(products);
                sumTransaction.setQuantity(quantity.getQuantity());
                sumTransaction.setSum(quantity.getQuantity() * products.getProductPrice());
                sumTransaction.setTransaction(transaction);
                quantities.add(quantity);
                sumTransactions.add(sumTransaction);
            }
            else{
                throw new RuntimeException("Maaf Persediaan Barang tidak cukup/stok habis");
            }
        }
        sumTransactionRepository.saveAll(sumTransactions);
        productRepository.saveAll(carts.getProducts());
        try {
            byte[] pdfContent = PdfUtil.createInvoice(transaction,sumTransactions);
            emailService.sendHtmlMessageWithAttachment(
                    users.getUsername(),
                    "Transaksi Berhasil!",
                    "Terimkasih telah berbelanja di Dapa Electronic Store. " +
                            "Kami sangat menghargai kepercayaan anda memilih produk kami." +
                            "Berikut kami lampirkan bukti invoice dari transaksi anda, " +
                            "kami berharap dapat melayani anda kembali di masa depan! " +
                            "Salam hangat, \nDapa Electronic Store",
                    "invoice.pdf",
                    pdfContent
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        cartRepository.deleteById(carts.getId());
        for(Quantity quantity: quantities){
            quantityRepository.delete(quantity);
        }
        return "Successfully add transaction";
    }
    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions;   
    }
    @Override
	public String deleteTransaction(String id) {
        transactionRepository.deleteById(id);
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        sumTransactionRepository.deleteAllByTransaction(transaction);
        return "success";
	}

}
