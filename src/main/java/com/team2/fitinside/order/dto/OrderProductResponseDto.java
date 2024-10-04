package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponseDto { // 주문 상품 DTO

    private Long productId;
    private String productName;
    private int productPrice;
    private int count;

}
