package com.dapa.dapa.dto.customer;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationRequestDto {

    private String customerName;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth; //"1990-10-25" YYYY-MM-DD ISO_8601
    private String gender;
    private String customerPhoto;

    //login
    String password;
}
