package com.dapa.dapa.entity;

import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="voucer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voucher {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;   

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "type", length = 100)
    private String type; // gratis ongkir

    @Column(name = "expirationDate")
    private LocalDate expirationDate;

}
