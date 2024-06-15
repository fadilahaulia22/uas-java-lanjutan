package com.dapa.dapa.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dapa.dapa.entity.Customer;
import com.dapa.dapa.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
       public List<Customer> getAllCustomers(){
            return customerRepository.findAll();
        }
}