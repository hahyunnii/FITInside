package com.team2.fitinside.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProductDto {
    private String productType;
    private String productName;
    private int price;
    private String info;
    private String manufacturer;
    private int categoryId;
}
