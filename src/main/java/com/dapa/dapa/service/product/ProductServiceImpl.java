package com.dapa.dapa.service.product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dapa.dapa.dao.product.ProductDao;
import com.dapa.dapa.dto.PageResponse;
import com.dapa.dapa.dto.product.ProductRequestDto;
import com.dapa.dapa.dto.product.ProductResponseDto;
import com.dapa.dapa.entity.Category;
import com.dapa.dapa.entity.Products;
import com.dapa.dapa.repository.CategoryRepository;
import com.dapa.dapa.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
        @Autowired
        ProductDao productDao;

        @Autowired
        ProductRepository productRepository;

        @Autowired
        CategoryRepository categoryRepository;

        @Override
        public PageResponse<ProductResponseDto> findAll(String productName, String productMerk, String productType,
                        String category, int page, int size, String sortBy, String sortOrder) {
                Category categoryName = categoryRepository.findByCategoryName(category);
                PageResponse<Products> productsPage = productDao.findAll(productName, productMerk, productType,
                                categoryName, page, size, sortBy, sortOrder);

                List<ProductResponseDto> productResponseDtos = productsPage.getItems().stream()
                                .map(this::toProductDto)
                                .collect(Collectors.toList());

                return PageResponse.success(productResponseDtos, productsPage.getPage(), productsPage.getSize(),
                                productsPage.getTotalItem());
        }

        @Override
        public String add(ProductRequestDto dto) {
                Products products = new Products();
                products.setProductName(dto.getProductName());
                products.setProductMerk(dto.getProductMerk());
                products.setProductType(dto.getProductType());
                products.setProductColor(dto.getProductColor());
                products.setProductPrice(dto.getProductPrice());
                products.setProductStock(dto.getProductStock());
                System.out.println(dto);
                Category category = categoryRepository.findByCategoryName(dto.getCategoryName());
                products.setCategory(category);
                productRepository.save(products);
                return "Successfully add new product";
        }

        @Override
        public String update(String id, ProductRequestDto dto) {
                Products products = productRepository.findById(id).orElse(null);
                products.setProductName(dto.getProductName());
                products.setProductMerk(dto.getProductMerk());
                products.setProductType(dto.getProductType());
                products.setProductColor(dto.getProductColor());
                products.setProductPrice(dto.getProductStock());
                products.setProductStock(dto.getProductStock());
                Category category = categoryRepository.findByCategoryName(dto.getCategoryName());
                products.setCategory(category);
                productRepository.save(products);
                System.out.println(dto);
                return "Successfully update product";
        }

        @Override
        public String remove(String id) {
                productRepository.deleteById(id);
                return "Successfully remove product";
        }

        @Override
        public List<ProductResponseDto> viewall() {
                List<Products> products = productRepository.findAll();
                return products.stream()
                                .map(this::toProductDto)
                                .collect(Collectors.toList());
        }

        public ProductResponseDto toProductDto(Products products) {
                return ProductResponseDto.builder()
                                .id(products.getId())
                                .productName(products.getProductName())
                                .productMerk(products.getProductMerk())
                                .productType(products.getProductType())
                                .productColor(products.getProductColor())
                                .productPrice(products.getProductPrice())
                                .productStock(products.getProductStock())
                                .category(products.getCategory().getCategoryName())
                                .build();
        }

        @Override
        public void uploadProductPhoto(String id, MultipartFile productPhoto) throws IOException, SQLException {
                String[] filename = Objects.requireNonNull(productPhoto.getResource().getFilename()).split("\\.");

                if (!filename[filename.length - 1].equalsIgnoreCase("jpg") &&
                                !filename[filename.length - 1].equalsIgnoreCase("jpeg") &&
                                !filename[filename.length - 1].equalsIgnoreCase("png")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsuppport file type");
                }

                Products products = productRepository.findById(id).orElse(null);
                if (products != null) {
                        products.setProductPhoto(new SerialBlob(productPhoto.getBytes()));
                        productRepository.save(products);
                } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Not Found");
                }
        }
}
