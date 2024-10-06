package com.team2.fitinside.product.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductUpdateDto {
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String info;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    @NotNull(message = "카테고리 ID는 필수 입력 값입니다.")
    private Long categoryId;

    @NotNull(message = "판매자 ID는 필수 입력 값입니다.")
    private Long userId;
}
