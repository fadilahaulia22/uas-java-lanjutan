package com.dapa.dapa.entity;


import java.sql.Blob;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "product_merk", length = 255)
    private String productMerk;

    @Column(name = "product_type", length = 255)
    private String productType;

    @Column(name = "product_color", length = 255)
    private String productColor;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_stock")
    private int productStock;

    @Lob
    @Column(name = "product_photo")
    private Blob productPhoto;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;    
}
