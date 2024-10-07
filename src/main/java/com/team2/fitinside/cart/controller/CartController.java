package com.team2.fitinside.cart.controller;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseWrapperDto> findCart() {

        CartResponseWrapperDto carts = cartService.findAllCarts();
        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }

    @PostMapping
    public ResponseEntity<String> createCart(@RequestBody CartCreateRequestDto dto) {

        cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("장바구니가 추가되었습니다!");
    }

    @PutMapping
    public ResponseEntity<String> updateCart(@RequestBody CartUpdateRequestDto dto) throws Exception {

        cartService.updateCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 수정되었습니다!");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteCart(@PathVariable("productId") Long productId) throws Exception {

        cartService.deleteCart(productId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 삭제되었습니다!");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart() {

        cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 초기화되었습니다!");
    }
}
