package com.dapa.dapa.entity;

import java.sql.Blob;
import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length =  25)
    private String gender;

    @Lob
    @Column(name = "photo")
    private Blob photo;

    @OneToOne
    @JoinColumn(name="users_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Users users;

}
