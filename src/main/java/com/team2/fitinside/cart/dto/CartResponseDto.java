package com.team2.fitinside.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponseDto {

    private Long productId;
    private int quantity;

    // 상품 옵션, 상품 썸네일 이미지 추가 예정 (기능 고도화)
}
