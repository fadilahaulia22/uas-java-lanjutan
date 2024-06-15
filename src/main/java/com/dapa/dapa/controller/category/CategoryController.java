package com.dapa.dapa.controller.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.dto.category.CategoryRequestDto;
import com.dapa.dapa.service.category.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/category")
@Tag(name = "Category")
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> add(@RequestBody CategoryRequestDto dto) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(categoryService.add(dto), "Add Successfully Category"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody CategoryRequestDto dto) {
        try {
            System.out.println(dto);
            return ResponseEntity.ok().body(GenericResponse.success(categoryService.update(id, dto), "Update Successfully Category"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> remove(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(categoryService.remove(id), "Remove Category Success"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get")
    public ResponseEntity<Object> viewall(){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(categoryService.viewall(), "Successfully fetch data"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
