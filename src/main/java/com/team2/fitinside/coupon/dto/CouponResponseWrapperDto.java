package com.team2.fitinside.coupon.dto;

import com.team2.fitinside.cart.dto.CartResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CouponResponseWrapperDto {

    private String message;
    private List<CouponResponseDto> coupons;
}
