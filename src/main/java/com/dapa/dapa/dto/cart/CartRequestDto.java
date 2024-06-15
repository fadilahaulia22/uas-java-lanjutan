package com.dapa.dapa.dto.cart;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {
    private String productId;
    private int quantity;
}
