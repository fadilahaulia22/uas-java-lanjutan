package com.dapa.dapa.service.cart;

import com.dapa.dapa.dto.cart.CartRequestDto;
import com.dapa.dapa.dto.cart.CartResponseDto;

public interface CartService {
    public String add(CartRequestDto dto);
    public CartResponseDto findAllCart();
    public String deleteCart(String id_cart,String id_product);
}
