package com.dapa.dapa.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dapa.dapa.constant.MessageConstant;
import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.dto.PageResponse;
import com.dapa.dapa.dto.product.ProductRequestDto;
import com.dapa.dapa.dto.product.ProductResponseDto;
import com.dapa.dapa.service.product.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/product")
@Tag(name = "Product")
@Slf4j
public class ProductContoller {

    @Autowired
    ProductService productService;

    @GetMapping("/get")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> findAll(
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String productMerk,
        @RequestParam(required = false) String productType,
        @RequestParam(required = false) String category,
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortOrder) {

    try {
        PageResponse<ProductResponseDto> response = productService.findAll(
                productName, 
                productMerk, 
                productType, 
                category, 
                page, 
                size,
                sortBy,
                sortOrder);

        return ResponseEntity.ok()
                .body(GenericResponse.success(response, "Successfully Fetch Data"));

    } catch (ResponseStatusException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));

    } catch (Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
    }
}

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> add(@RequestBody ProductRequestDto dto) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(productService.add(dto), "Success Add Data"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody ProductRequestDto dto) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(productService.update(id, dto),"Success Update Data"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public String remove(@PathVariable String id) {
        return productService.remove(id);
    }

    @PostMapping(value = "/upload-product-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> uploadProductPhoto(@RequestParam String id,
            @RequestParam("productPhoto") MultipartFile file) {
        try {
            productService.uploadProductPhoto(id, file);
            return ResponseEntity.ok()
                    .body(GenericResponse.success(null, "Succesfully upload product photo"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode())
                    .body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(MessageConstant.ERROR_500));
        }
    }
}
