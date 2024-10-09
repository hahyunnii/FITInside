package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponseDto {
    private String orderProductName;
    private int orderProductPrice;
    private int count;
}
