package com.dapa.dapa.controller.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dapa.dapa.constant.MessageConstant;
import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.dto.customer.CustomerRegistrationRequestDto;
import com.dapa.dapa.entity.Customer;
import com.dapa.dapa.service.EmailService;
import com.dapa.dapa.service.customer.RegistrationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/customer")
@Tag(name = "Customer")
@Slf4j
public class CustomerController {
    @Autowired
    RegistrationService registrationService;
    
    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody CustomerRegistrationRequestDto dto){
        try{
            Customer customer = registrationService.register(dto);
            return ResponseEntity.ok()
                    .body(GenericResponse.success(customer, 
                    "Successfully register new customer"));
        }catch(Exception e){
            return ResponseEntity.internalServerError()
                    .body(GenericResponse.error(e.getMessage()));

        }
    }
    
    @PostMapping(value = "/upload-customer-photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadCustomerPhoto(@RequestParam String customerId,@RequestParam("customerPhoto") MultipartFile file){
        try {
            registrationService.uploadCustomerPhoto(customerId,file);
            return ResponseEntity.ok().body(GenericResponse.success(null, "successfully upload customer photo"));
        }
        catch (ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        }
         catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(MessageConstant.ERROR_500));
        }
    }

}
