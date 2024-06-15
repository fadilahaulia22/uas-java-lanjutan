package com.dapa.dapa.service.category;

import java.util.List;

import com.dapa.dapa.dto.category.CategoryRequestDto;
import com.dapa.dapa.dto.category.CategoryResponseDto;

public interface CategoryService {
    String add(CategoryRequestDto dto);
    String update(String id , CategoryRequestDto dto);
    String remove(String id);
    List<CategoryResponseDto> viewall();
}
