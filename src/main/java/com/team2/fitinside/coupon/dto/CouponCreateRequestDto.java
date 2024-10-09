package com.team2.fitinside.coupon.dto;

import com.team2.fitinside.coupon.entity.CouponType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponCreateRequestDto {

    private CouponType type;
    private int value;
    private int percentage;
    private int minValue;
    private LocalDate expiredAt;
    private Long categoryId;
}
