package com.dapa.dapa.dto.transaction;
import java.util.List;

import com.dapa.dapa.dto.product.ProductResponseDto;
import com.dapa.dapa.entity.Quantity;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    String id;
    String transactionDate;
    List<Quantity> quantity;
    private String username;
    List<ProductResponseDto> products;
}
