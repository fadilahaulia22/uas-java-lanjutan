package com.dapa.dapa.controller.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.dto.cart.CartRequestDto;
import com.dapa.dapa.service.cart.CartService;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> findAll(){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(cartService.findAllCart(), "Successfully Fetch Data"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addCart(@RequestBody CartRequestDto dto){
        try {
            System.out.println(dto);
            return ResponseEntity.ok().body(GenericResponse.success(cartService.add(dto), "Successfully Add Cart"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok().body(GenericResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> remove(@RequestParam String id_cart,@RequestParam String id_product){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(cartService.deleteCart(id_cart,id_product), "Successfully delete cart"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
