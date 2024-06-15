package com.dapa.dapa.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @UuidGenerator
    @Column(name = "id",length = 36,nullable = false)
    private String id;
    
    @ManyToMany
    @JoinTable(name = "cart_products",joinColumns = @JoinColumn(name = "cart_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Products> products = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    private Users users;
}
