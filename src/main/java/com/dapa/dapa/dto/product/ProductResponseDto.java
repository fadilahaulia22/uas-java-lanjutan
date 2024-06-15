package com.dapa.dapa.dto.product;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String id;
    private String productName;
    private String productMerk;
    private String productType;
    private String productColor;
    private int productPrice;
    private int productStock;
    private String category;
}
