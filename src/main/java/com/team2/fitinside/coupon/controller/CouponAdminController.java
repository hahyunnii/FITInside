package com.team2.fitinside.coupon.controller;

import com.team2.fitinside.coupon.dto.CouponCreateRequestDto;
import com.team2.fitinside.coupon.dto.CouponMemberResponseWrapperDto;
import com.team2.fitinside.coupon.dto.CouponResponseWrapperDto;
import com.team2.fitinside.coupon.service.CouponAdminService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없는 사용자입니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
})
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @GetMapping
    public ResponseEntity<CouponResponseWrapperDto> findAllCoupons(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestParam(required = false, value = "includeInActiveCoupons", defaultValue = "false") boolean includeInActiveCoupons) {

        CouponResponseWrapperDto allCoupons = couponAdminService.findAllCoupons(page, includeInActiveCoupons);
        return ResponseEntity.status(HttpStatus.OK).body(allCoupons);
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponMemberResponseWrapperDto> findCouponMembers(
            @PathVariable("couponId") Long couponId,
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {

        CouponMemberResponseWrapperDto allMembers = couponAdminService.findCouponMembers(page, couponId);
        return ResponseEntity.status(HttpStatus.OK).body(allMembers);
    }

    @PostMapping
    public ResponseEntity<String> createCoupon(@RequestBody CouponCreateRequestDto couponCreateRequestDto) {

        couponAdminService.createCoupon(couponCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰이 추가되었습니다!");
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deActiveCoupon(@PathVariable("couponId") Long couponId) {

        couponAdminService.deActiveCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 비활성화 되었습니다!");
    }
}
