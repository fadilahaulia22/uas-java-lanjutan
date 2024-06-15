package com.dapa.dapa.service.product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dapa.dapa.dto.PageResponse;
import com.dapa.dapa.dto.product.ProductRequestDto;
import com.dapa.dapa.dto.product.ProductResponseDto;


public interface ProductService {
    PageResponse<ProductResponseDto> findAll(String productName, String productMerk, String productType,
            String category, int page, int size, String sortBy, String sortOrder);

    String add(ProductRequestDto dto);
    String update(String id, ProductRequestDto dto);
    String remove(String id);
    List<ProductResponseDto> viewall();
    void uploadProductPhoto(String id, MultipartFile productPhoto) throws IOException, SQLException;
}
