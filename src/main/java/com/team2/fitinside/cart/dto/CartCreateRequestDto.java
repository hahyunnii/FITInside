package com.team2.fitinside.cart.dto;

import lombok.Data;

@Data
public class CartCreateRequestDto {

    private Long productId;
    private int quantity;
}
