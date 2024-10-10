package com.team2.fitinside.coupon.controller;

import com.team2.fitinside.coupon.dto.AvailableCouponResponseWrapperDto;
import com.team2.fitinside.coupon.dto.CouponResponseWrapperDto;
import com.team2.fitinside.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없는 사용자입니다."),
        @ApiResponse(responseCode = "500", description = "서버 에러")
})
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    @Operation(summary = "보유 쿠폰 목록 조회", description = "보유 쿠폰 목록 조회")
    @ApiResponse(responseCode = "200", description = "쿠폰 목록 조회 완료했습니다!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponseWrapperDto.class)))
    public ResponseEntity<CouponResponseWrapperDto> findAllCoupons(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestParam(required = false, value = "includeInActiveCoupons", defaultValue = "false") boolean includeInActiveCoupons) throws AccessDeniedException {

        CouponResponseWrapperDto allCoupons = couponService.findAllCoupons(page, includeInActiveCoupons);
        return ResponseEntity.status(HttpStatus.OK).body(allCoupons);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "적용 가능 쿠폰 목록 조회", description = "상품에 적용 가능한 쿠폰 목록 조회")
    @ApiResponse(responseCode = "200", description = "쿠폰 목록 조회 완료했습니다!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailableCouponResponseWrapperDto.class)))
    public ResponseEntity<AvailableCouponResponseWrapperDto> findAllAvailableCoupons(@PathVariable("productId") Long productId) {

        AvailableCouponResponseWrapperDto allAvailableCoupons = couponService.findAllAvailableCoupons(productId);
        return ResponseEntity.status(HttpStatus.OK).body(allAvailableCoupons);
    }

    @PostMapping
    @Operation(summary = "쿠폰 입력", description = "쿠폰 입력")
    @ApiResponse(responseCode = "201", description = "쿠폰이 입력되었습니다!", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "쿠폰 정보가 유효하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409", description = "쿠폰 등록 이력이 존재합니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> enterCouponCode(@RequestBody String code) {

        couponService.enterCouponCode(code);
        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰이 입력되었습니다!");
    }

    @PostMapping("/{couponMemberId}")
    @Operation(summary = "쿠폰 적용", description = "상품에 쿠폰 적용")
    @ApiResponse(responseCode = "200", description = "쿠폰이 사용되었습니다!", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "쿠폰 정보가 유효하지 않습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> redeemCoupon(@PathVariable("couponMemberId") Long couponMemberId) {

        couponService.redeemCoupon(couponMemberId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 사용되었습니다!");
    }
}
