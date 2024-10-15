package com.team2.fitinside.coupon.dto;

import lombok.Data;

@Data
public class CouponEmailRequestDto {

    private Long couponId;
    private String address;
    private String template;
}
