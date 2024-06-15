package com.dapa.dapa.service.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dapa.dapa.dto.cart.CartRequestDto;
import com.dapa.dapa.dto.cart.CartResponseDto;
import com.dapa.dapa.dto.product.ProductResponseDto;
import com.dapa.dapa.entity.Cart;
import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.Quantity;
import com.dapa.dapa.entity.Users;
import com.dapa.dapa.repository.CartRepository;
import com.dapa.dapa.repository.ProductRepository;
import com.dapa.dapa.repository.QuantityRepository;
import com.dapa.dapa.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {
        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private QuantityRepository quantityRepository;

        @Override
        public String add(CartRequestDto dto) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                Users user = userRepository.findUsersByUsername(username);
                Cart cart2 = cartRepository.findByUsers(user);
                Quantity quantity = new Quantity();
                Products products = productRepository.findOneById(dto.getProductId());
                if (cart2 != null) {
                        quantity.setUsers(user);
                        quantity.setProducts(products);
                        quantity.setQuantity(dto.getQuantity());
                        cart2.getProducts().add(products);
                        cart2.setUsers(user);
                        cartRepository.save(cart2);
                } else {
                        quantity.setUsers(user);
                        quantity.setProducts(products);
                        quantity.setQuantity(dto.getQuantity());
                        Cart cart = new Cart();
                        cart.setUsers(user);
                        cart.getProducts().add(products);
                        cartRepository.save(cart);
                }
                quantityRepository.save(quantity);
                return "Successfully Add Cart";
        }

        @Override
        public CartResponseDto findAllCart() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                Users users = userRepository.findUsersByUsername(username);
                Cart carts = cartRepository.findByUsers(users);
                CartResponseDto responseDto = new CartResponseDto();
                responseDto.setId(carts.getId());
                responseDto.setUsername(username);
                List<Quantity> quantities = new ArrayList<>();
                for (Products products : carts.getProducts()) {
                        quantities.add(quantityRepository.findByUsersAndProducts(users, products));
                }
                responseDto.setQuantity(quantities);
                List<ProductResponseDto> products = carts.getProducts().stream()
                                .map(this::toProductResponseDto).collect(Collectors.toList());
                responseDto.setProduct(products);
                return responseDto;
        }

        private ProductResponseDto toProductResponseDto(Products products) {
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
        @Transactional
        public String deleteCart(String id_cart,String id_product) {
                Optional<Cart> cartOptional = cartRepository.findById(id_cart);
                
                cartOptional.ifPresent(cart -> {
                        Set<Products> products = cart.getProducts();
        
                        products.removeIf(product -> product.getId().equals(id_product));
        
                        cartRepository.save(cart);
                });
                if(cartOptional.isPresent()){
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        String username = auth.getName();
                        Users users = userRepository.findUsersByUsername(username);
                        Products products = productRepository.findProductsById(id_product);
                        quantityRepository.deleteByProductsAndUsers(products, users);
                        if(cartRepository.findByUsers(users).equals(null)){
                                cartRepository.deleteAllByUsers(users);
                        }
                        return "success";
                }
                return "failed";
        }
}