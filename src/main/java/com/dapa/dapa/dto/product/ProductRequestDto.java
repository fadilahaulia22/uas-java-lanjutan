package com.dapa.dapa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String productName;
    private String productMerk;
    private String productType;
    private String productColor;
    private int productPrice;
    private int productStock;
    private String categoryName;
}
