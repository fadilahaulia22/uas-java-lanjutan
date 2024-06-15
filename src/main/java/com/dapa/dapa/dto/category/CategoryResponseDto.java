package com.dapa.dapa.dto.category;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private String id;
    private String categoryName;
}
