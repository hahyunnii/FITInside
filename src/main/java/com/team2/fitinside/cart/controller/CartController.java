package com.team2.fitinside.cart.controller;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.exception.CartDuplicatedException;
import com.team2.fitinside.cart.exception.CartOutOfRangeException;
import com.team2.fitinside.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseWrapperDto> findCart() {
        try {
            CartResponseWrapperDto carts = cartService.findAllCarts();
            return ResponseEntity.status(HttpStatus.OK).body(carts);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CartResponseWrapperDto(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CartResponseWrapperDto("장바구니 조회 중 서버 에러 발생!"));
        }
    }

    @PostMapping
    public ResponseEntity<String> createCart(@RequestBody CartCreateRequestDto dto) {
        try {
            cartService.createCart(dto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (CartDuplicatedException | CartOutOfRangeException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 조회 중 서버 에러 발생!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 추가되었습니다!");
    }

    @PutMapping
    public ResponseEntity<String> updateCart(@RequestBody CartUpdateRequestDto dto) {
        try {
            cartService.updateCart(dto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (CartOutOfRangeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 조회 중 서버 에러 발생!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 수정되었습니다!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestParam Long id) {
        try {
            cartService.deleteCart(id);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 조회 중 서버 에러 발생!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 삭제되었습니다!");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        try {
            cartService.clearCart();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 조회 중 서버 에러 발생!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 초기화되었습니다!");
    }
}
