package com.team2.fitinside.cart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartProductResponseDto {

    private String productName;
    private int price;
    private int quantity;

}
