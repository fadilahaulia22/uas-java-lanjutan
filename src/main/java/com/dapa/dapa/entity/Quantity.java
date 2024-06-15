package com.dapa.dapa.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Quantity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quantity {
    @Id
    @UuidGenerator
    @Column(name = "id",length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "users_id",referencedColumnName = "id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "products_id",referencedColumnName = "id")
    private Products products;

    @Column(name = "quantity")
    private int quantity;
}
