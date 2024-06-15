package com.dapa.dapa.dto.cart;

import java.util.List;

import com.dapa.dapa.dto.product.ProductResponseDto;
import com.dapa.dapa.entity.Quantity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private String id;
    private List<Quantity> quantity;
    private String username;
    List<ProductResponseDto> product;
}
