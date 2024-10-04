package com.team2.fitinside.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ProductCreateDto {
    private String productName;
    private int price;
    private String manufacturer;
    private Long categoryId;
}

