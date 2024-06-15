package com.dapa.dapa.dao.product;

import com.dapa.dapa.dto.PageResponse;
import com.dapa.dapa.entity.Category;
import com.dapa.dapa.entity.Products;

public interface ProductDao {
    PageResponse<Products> findAll(String productName, String productMerk, String productType,
    Category category, int page, int size, String sortBy, String sortOrder);
}
