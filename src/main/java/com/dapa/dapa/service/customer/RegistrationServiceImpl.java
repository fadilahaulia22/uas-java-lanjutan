package com.dapa.dapa.service.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dapa.dapa.constant.RolesConstant;
import com.dapa.dapa.dto.customer.CustomerRegistrationRequestDto;
import com.dapa.dapa.entity.Customer;
import com.dapa.dapa.entity.Roles;
import com.dapa.dapa.entity.Users;
import com.dapa.dapa.repository.CustomerRepository;
import com.dapa.dapa.repository.RolesRepository;
import com.dapa.dapa.repository.UserRepository;
import com.dapa.dapa.service.EmailService;

@Service

public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    RolesRepository repository;

    @Autowired
    UserRepository usersRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Customer register(CustomerRegistrationRequestDto dto) {
        if(usersRepository.findUsersByUsername(dto.getEmail()) == null){
            Users user = saveUsers(dto);
            Roles roles = repository.findByRoleName(RolesConstant.ACCOUNTANT_ROLE);
            Roles roles2 = repository.findByRoleName(RolesConstant.WAREHOUSE_ROLE);
            if (usersRepository.findByRoles(roles) == null) {
                saveAccountant();
            }
            if (usersRepository.findByRoles(roles2) == null) {
                saveWarehouse();
            }
            Customer customer = saveCustomer(dto, user);
            emailService.sendEmail(dto.getEmail());
            // sendEmail(dto.getEmail());
            return customer;
        }
        else{
            throw new RuntimeException("Email telah digunakan");
        }
    }

    private Customer saveCustomer(CustomerRegistrationRequestDto dto, Users users) {
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        LocalDate dateOfBirth = LocalDate.parse(dto.getDateOfBirth(),
                DateTimeFormatter.ISO_DATE);
        customer.setDateOfBirth(dateOfBirth);
        customer.setGender(dto.getGender());
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setRegistrationDate(LocalDate.now());
        customer.setUsers(users);
        return customerRepository.save(customer);
    }

    private void saveAccountant() {
        Users users = new Users();
        users.setUsername("accountant123");
        users.setPassword(passwordEncoder.encode("12345678"));
        Roles customerRoles = repository.findByRoleName(RolesConstant.ACCOUNTANT_ROLE);
        users.setRoles(customerRoles);
        usersRepository.save(users);
    }

    private void saveWarehouse() {
        Users users = new Users();
        users.setUsername("warehouse123");
        users.setPassword(passwordEncoder.encode("12345678"));
        Roles customerRoles = repository.findByRoleName(RolesConstant.WAREHOUSE_ROLE);
        users.setRoles(customerRoles);
        usersRepository.save(users);
    }

    private Users saveUsers(CustomerRegistrationRequestDto dto) {
        Users users = new Users();
        users.setUsername(dto.getEmail());
        users.setPassword(passwordEncoder.encode(dto.getPassword()));
        Roles customerRoles = repository.findByRoleName(RolesConstant.CUSTOMER_ROLE);
        users.setRoles(customerRoles);
        return usersRepository.save(users);
    }

    // PHOTO

    @Override
    public void uploadCustomerPhoto(String customerId, MultipartFile customerPhoto) throws IOException, SQLException {

        String[] filename = Objects.requireNonNull(customerPhoto.getResource().getFilename()).split("\\.");

        if (!filename[filename.length - 1].equalsIgnoreCase("jpg")
                && !filename[filename.length - 1].equalsIgnoreCase("jpeg")
                && !filename[filename.length - 1].equalsIgnoreCase("png")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type");
        }

        System.out.println(filename);

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            customer.setPhoto(new SerialBlob(customerPhoto.getBytes()));
            customerRepository.save(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not Found");
        }
    }

}
