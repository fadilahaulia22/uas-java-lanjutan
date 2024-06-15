package com.dapa.dapa.service.customer;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import com.dapa.dapa.dto.customer.CustomerRegistrationRequestDto;
import com.dapa.dapa.entity.Customer;

public interface RegistrationService {
    Customer register(CustomerRegistrationRequestDto dto);

    void uploadCustomerPhoto(String customerId, MultipartFile photo)
        throws IOException, SQLException;
    

}
