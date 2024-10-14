package com.team2.fitinside.cart.controller;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
})
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "로그인한 회원의 장바구니 조회", description = "장바구니 조회")
    @ApiResponse(responseCode = "200", description = "장바구니 조회 완료했습니다!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseWrapperDto.class)))
    public ResponseEntity<CartResponseWrapperDto> findCart() {

        CartResponseWrapperDto carts = cartService.findAllCarts();
        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }

    @PostMapping
    @Operation(summary = "로그인한 회원의 장바구니 추가", description = "장바구니 추가")
    @ApiResponse(responseCode = "201", description = "장바구니가 추가되었습니다!", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "회원 또는 상품 존재하지 않거나 상품 수량 범위 예외", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> createCart(@RequestBody CartCreateRequestDto dto) {

        cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("장바구니가 추가되었습니다!");
    }

    @PutMapping
    @Operation(summary = "로그인한 회원의 장바구니 수정", description = "장바구니 수정")
    @ApiResponse(responseCode = "200", description = "장바구니가 수정되었습니다!", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "상품 수량 범위 예외", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> updateCart(@RequestBody CartUpdateRequestDto dto) {

        cartService.updateCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 수정되었습니다!");
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "로그인한 회원의 장바구니 단건 삭제", description = "장바구니 삭제")
    @ApiResponse(responseCode = "200", description = "장바구니가 삭제되었습니다!", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "장바구니 존재 x", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> deleteCart(@PathVariable("productId") Long productId) {

        cartService.deleteCart(productId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 삭제되었습니다!");
    }

    @DeleteMapping
    @Operation(summary = "로그인한 회원의 장바구니 초기화", description = "장바구니 초기화")
    @ApiResponse(responseCode = "200", description = "장바구니가 초기화되었습니다!", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> clearCart() {

        cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 초기화되었습니다!");
    }
}
