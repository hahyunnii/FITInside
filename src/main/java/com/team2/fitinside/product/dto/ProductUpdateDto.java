package com.team2.fitinside.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateDto {
    private Long id; // 상품 ID
    private String productName;
    private int price;
    private String manufacturer;
    private Long categoryId;
}