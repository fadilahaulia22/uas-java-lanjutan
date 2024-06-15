package com.dapa.dapa.service.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dapa.dapa.dto.category.CategoryRequestDto;
import com.dapa.dapa.dto.category.CategoryResponseDto;
import com.dapa.dapa.entity.Category;
import com.dapa.dapa.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

        @Autowired
        CategoryRepository categoryRepository;

        @Override
        public List<CategoryResponseDto> viewall() {
                return categoryRepository.findAll()
                                .stream()
                                .map(this::toCategoryDto)
                                .toList();
        }

        @Override
        public String add(CategoryRequestDto dto) {
                Category category = new Category();
                category.setCategoryName(dto.getCategoryName());
                categoryRepository.save(category);
                return "Successfully add new category";
        }

        @Override
        public String update(String id, CategoryRequestDto dto) {
                Category category = categoryRepository.findById(id).orElse(null);
                if (category != null) {
                        category.setCategoryName(dto.getCategoryName());
                        categoryRepository.save(category);
                        return "Successfully update category";
                } else {
                        return "Id not found";
                }
        }

        @Override
        public String remove(String id) {
                Category category = categoryRepository.findById(id).orElse(null);
                if (category != null) {
                        categoryRepository.delete(category);
                        return "Successfully remove category";

                } else {
                        return "Id not Found";
                }
        }

        public CategoryResponseDto toCategoryDto(Category category) {
                return CategoryResponseDto.builder()
                                .id(category.getId())
                                .categoryName(category.getCategoryName())
                                .build();
        }

}
