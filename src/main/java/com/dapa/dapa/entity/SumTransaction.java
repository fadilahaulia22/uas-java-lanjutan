package com.dapa.dapa.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sum_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumTransaction {
    @Id
    @UuidGenerator
    @Column(name = "id",length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "products_id",referencedColumnName = "id")
    private Products products;

    @ManyToOne
    @JoinColumn(name = "transaction_id",referencedColumnName = "id")
    private Transaction transaction;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "sum")
    private int sum;

    public SumTransaction findByUsersAndProducts(Users users, Products products2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsersAndProducts'");
    }
}
