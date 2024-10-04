package com.team2.fitinside.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private String productName;
    private int price;
    private String manufacturer;
    private String categoryName; // 카테고리 이름 포함
}