package com.team2.fitinside.coupon.controller;

import com.team2.fitinside.coupon.dto.AvailableCouponResponseWrapperDto;
import com.team2.fitinside.coupon.dto.CouponResponseWrapperDto;
import com.team2.fitinside.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<CouponResponseWrapperDto> findAllCoupons(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestParam(required = false, value = "includeInActiveCoupons", defaultValue = "false") boolean includeInActiveCoupons) throws AccessDeniedException {

        CouponResponseWrapperDto allCoupons = couponService.findAllCoupons(page, includeInActiveCoupons);
        return ResponseEntity.status(HttpStatus.OK).body(allCoupons);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AvailableCouponResponseWrapperDto> findAllAvailableCoupons(@PathVariable("productId") Long productId) throws AccessDeniedException {

        AvailableCouponResponseWrapperDto allAvailableCoupons = couponService.findAllAvailableCoupons(productId);
        return ResponseEntity.status(HttpStatus.OK).body(allAvailableCoupons);
    }

    @PostMapping
    public ResponseEntity<String> enterCouponCode(@RequestBody String code) throws AccessDeniedException {

        couponService.enterCouponCode(code);
        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰이 입력되었습니다!");
    }

    @PostMapping("/{couponMemberId}")
    public ResponseEntity<String> redeemCoupon(@PathVariable("couponMemberId") Long couponMemberId) {

        couponService.redeemCoupon(couponMemberId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 사용되었습니다!");
    }
}
