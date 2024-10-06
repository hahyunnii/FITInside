package com.team2.fitinside.cart.controller;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.exception.CartOutOfRangeException;
import com.team2.fitinside.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseWrapperDto> findCart() {

        return callMethodAndHandleException(() -> {
            CartResponseWrapperDto carts = cartService.findAllCarts();
            return ResponseEntity.status(HttpStatus.OK).body(carts);
        }, "장바구니 조회 중 서버 에러 발생!");
    }

    @PostMapping
    public ResponseEntity<String> createCart(@RequestBody CartCreateRequestDto dto) {

        return callMethodAndHandleException(() -> {
            cartService.createCart(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("장바구니가 추가되었습니다!");
        }, "장바구니 추가 중 서버 에러 발생!");
    }

    @PutMapping
    public ResponseEntity<String> updateCart(@RequestBody CartUpdateRequestDto dto) {

        return callMethodAndHandleException(() -> {
            cartService.updateCart(dto);
            return ResponseEntity.status(HttpStatus.OK).body("장바구니가 수정되었습니다!");
        }, "장바구니 수정 중 서버 에러 발생!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable("id") Long id) {

        return callMethodAndHandleException(() -> {
            cartService.deleteCart(id);
            return ResponseEntity.status(HttpStatus.OK).body("장바구니가 삭제되었습니다!");
        }, "장바구니 삭제 중 서버 에러 발생!");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart() {

        return callMethodAndHandleException(() -> {
            cartService.clearCart();
            return ResponseEntity.status(HttpStatus.OK).body("장바구니가 초기화되었습니다!");
        }, "장바구니 초기화 중 서버 에러 발생!");
    }

    // 메서드를 호출하고 공통 예외 처리 메서드
    private <T> ResponseEntity<T> callMethodAndHandleException(Callable<ResponseEntity<T>> action, String message) {
        try {
            return action.call();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body((T) e.getMessage());
        } catch (CartOutOfRangeException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((T) message);
        }
    }
}
