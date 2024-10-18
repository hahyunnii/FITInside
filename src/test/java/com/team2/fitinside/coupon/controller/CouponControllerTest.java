package com.team2.fitinside.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.fitinside.coupon.dto.*;
import com.team2.fitinside.coupon.entity.Coupon;
import com.team2.fitinside.coupon.entity.CouponType;
import com.team2.fitinside.coupon.mapper.CouponMapper;
import com.team2.fitinside.coupon.service.CouponService;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CouponController.class)
@AutoConfigureMockMvc(addFilters = false) // 필터 제외 (JWT 검증 제외)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("쿠폰 회원 컨트롤러 단위 테스트")
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    // 기본 장바구니 컨트롤러의 url
    private static final String URL = "/api/coupons";

    @Autowired
    private ObjectMapper objectMapper;

    private static final int PAGE = 1;

    private Coupon coupon1;
    private Coupon coupon2;

    @BeforeEach
    void setUp() {
        // 테스트용 쿠폰 생성
        // 유효한 쿠폰
        coupon1 = Coupon.builder().id(1L).name("coupon1").code("AAAAAA").type(CouponType.AMOUNT).value(10000).minValue(0).expiredAt(LocalDate.of(2024, 10, 27)).build();
        // 유효하지 않은 쿠폰
        coupon2 = Coupon.builder().id(2L).name("coupon2").code("BBBBBB").type(CouponType.PERCENTAGE).percentage(20).minValue(30000).expiredAt(LocalDate.of(2024, 10, 20)).active(false).build();
    }

    @Test
    @Order(1)
    @DisplayName("쿠폰 목록 조회 - 유효한 쿠폰만 조회")
    void findAllActiveCoupons() throws Exception {

        //given
        boolean includeInActiveCoupons = false; // 유효하지 않은 쿠폰은 제외

        // 테스트용 쿠폰 응답 dto 생성 => 매퍼 사용
        CouponResponseDto dto1 = CouponMapper.INSTANCE.toCouponResponseDto(coupon1);

        // couponService.findAllCoupons() 호출 시 CouponResponseWrapperDto 반환하게 설정
        given(couponService.findAllCoupons(PAGE, includeInActiveCoupons))
                .willReturn(
                        new CouponResponseWrapperDto("쿠폰 목록 조회 완료했습니다!", List.of(dto1), 1));

        //when
        // page, includeInActiveCoupons는 생략 가능 (defaultValue 존재하므로)
        ResultActions resultActions = mockMvc.perform(get(URL + "?includeInActiveCoupons=" + includeInActiveCoupons));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value("쿠폰 목록 조회 완료했습니다!"))
                .andExpect(jsonPath("$.coupons.length()").value(1))
                .andExpect(jsonPath("$.coupons[0].id").value(dto1.getId()))
                .andExpect(jsonPath("$.coupons[0].name").value(dto1.getName()))
                .andExpect(jsonPath("$.coupons[0].code").value(dto1.getCode()))
                .andExpect(jsonPath("$.coupons[0].type").value(dto1.getType().toString()))
                .andExpect(jsonPath("$.coupons[0].value").value(dto1.getValue()))
                .andExpect(jsonPath("$.coupons[0].percentage").value(dto1.getPercentage()))
                .andExpect(jsonPath("$.coupons[0].minValue").value(dto1.getMinValue()))
                .andExpect(jsonPath("$.coupons[0].active").value(dto1.isActive()))
                .andExpect(jsonPath("$.coupons[0].expiredAt").value(dto1.getExpiredAt().toString()))
                .andExpect(jsonPath("$.coupons[0].categoryName").value(dto1.getCategoryName()))
                .andExpect(jsonPath("$.coupons[0].used").value(dto1.isUsed()));
    }

    @Test
    @Order(2)
    @DisplayName("쿠폰 목록 조회 - 전체 쿠폰 조회")
    void findAllCoupons() throws Exception {

        //given
        boolean includeInActiveCoupons = true; // 유효하지 않은 쿠폰은 제외

        // 테스트용 쿠폰 응답 dto 생성 => 매퍼 사용
        CouponResponseDto dto1 = CouponMapper.INSTANCE.toCouponResponseDto(coupon1);
        CouponResponseDto dto2 = CouponMapper.INSTANCE.toCouponResponseDto(coupon2);

        // couponService.findAllCoupons() 호출 시 CouponResponseWrapperDto 반환하게 설정
        given(couponService.findAllCoupons(PAGE, includeInActiveCoupons))
                .willReturn(
                        new CouponResponseWrapperDto("쿠폰 목록 조회 완료했습니다!", List.of(dto1, dto2), 1));

        //when
        // page, includeInActiveCoupons는 생략 가능 (defaultValue 존재하므로)
        ResultActions resultActions = mockMvc.perform(get(URL + "?includeInActiveCoupons=" + includeInActiveCoupons));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value("쿠폰 목록 조회 완료했습니다!"))
                .andExpect(jsonPath("$.coupons.length()").value(2))
                .andExpect(jsonPath("$.coupons[0].active").value(dto1.isActive()))
                .andExpect(jsonPath("$.coupons[1].active").value(dto2.isActive()));
    }

    @Test
    @Order(3)
    @DisplayName("쿠폰 목록 조회 - 403에러 (권한 없는 경우)")
    public void findAllCoupons403Exception() throws Exception {

        //given
        CustomException authorizedException = new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        given(couponService.findAllCoupons(PAGE, false))
                .willThrow(authorizedException);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL));

        //then
        resultActions
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.code").value(authorizedException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("권한이 없는 사용자입니다."));
    }

    @Test
    @Order(4)
    @DisplayName("적용 가능 쿠폰 목록 조회")
    void findAllAvailableCoupons() throws Exception{

        //given
        Long productId = 1L;
        String message = "쿠폰 목록 조회 완료했습니다!";
        // 테스트용 AvailableCouponResponseDto 생성 (2개)
        AvailableCouponResponseDto dto1 = CouponMapper.INSTANCE.toAvailableCouponResponseDto(coupon1);
        dto1.setCouponMemberId(1L);
        AvailableCouponResponseDto dto2 = CouponMapper.INSTANCE.toAvailableCouponResponseDto(coupon2);
        dto2.setCouponMemberId(2L);

        // couponService.findAllAvailableCoupons가 AvailableCouponResponseWrapperDto를 반환하게 설정
        given(couponService.findAllAvailableCoupons(productId))
                .willReturn(new AvailableCouponResponseWrapperDto(message, List.of(dto1, dto2)));

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/" + productId));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.coupons.length()").value(2))
                .andExpect(jsonPath("$.coupons[0].name").value(dto1.getName()))
                .andExpect(jsonPath("$.coupons[0].couponMemberId").value(dto1.getCouponMemberId()))
                .andExpect(jsonPath("$.coupons[0].type").value(dto1.getType().toString()))
                .andExpect(jsonPath("$.coupons[0].value").value(dto1.getValue()))
                .andExpect(jsonPath("$.coupons[0].percentage").value(dto1.getPercentage()))
                .andExpect(jsonPath("$.coupons[0].minValue").value(dto1.getMinValue()))
                .andExpect(jsonPath("$.coupons[0].expiredAt").value(dto1.getExpiredAt().toString()));
    }

    @Test
    @Order(5)
    @DisplayName("적용 가능 쿠폰 목록 조회 - 404에러 (상품을 찾을 수 없는 경우)")
    void findAllAvailableCoupons404Exception() throws Exception{

        //given
        Long productId = 1L;

        CustomException productNotFoundException = new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        // couponService.findAllAvailableCoupons가 PRODUCT_NOT_FOUND 에러를 반환하게 설정
        given(couponService.findAllAvailableCoupons(productId))
                .willThrow(productNotFoundException);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/" + productId));

        //then
        resultActions
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.code").value(productNotFoundException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("해당 상품을 찾을 수 없습니다."));
    }

    @Test
    @Order(6)
    @DisplayName("쿠폰 검색")
    void findCoupon() throws Exception{

        //given
        String couponCode = coupon1.getCode();
        CouponResponseDto dto = CouponMapper.INSTANCE.toCouponResponseDto(coupon1);

        // couponService.findCoupon가 CouponResponseDto를 반환하게 설정
        given(couponService.findCoupon(couponCode)).willReturn(dto);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/code/" + couponCode));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.code").value(dto.getCode()))
                .andExpect(jsonPath("$.type").value(dto.getType().toString()));
    }

    @Test
    @Order(7)
    @DisplayName("쿠폰 검색 - 400에러 (쿠폰 정보가 유효하지 않은 경우)")
    void findCoupon400Exception() throws Exception{

        //given
        String couponCode = "ERROR1";

        CustomException invalidCouponDataException = new CustomException(ErrorCode.INVALID_COUPON_DATA);

        // couponService.findCoupon가 INVALID_COUPON_DATA 에러를 반환하게 설정
        given(couponService.findCoupon(couponCode)).willThrow(invalidCouponDataException);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/code/" + couponCode));

        //then
        resultActions
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(invalidCouponDataException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("쿠폰 정보가 유효하지 않습니다."));

    }

    @Test
    @Order(8)
    @DisplayName("웰컴 쿠폰 검색")
    void findWelcomeCoupons() throws Exception{

        //given
        String message = "쿠폰 목록 조회 완료했습니다!";

        CouponResponseDto dto1 = CouponMapper.INSTANCE.toCouponResponseDto(coupon1);
        CouponResponseDto dto2 = CouponMapper.INSTANCE.toCouponResponseDto(coupon2);

        // couponService.findWelcomeCoupons()이 CouponResponseWrapperDto를 반환하게 설정
        given(couponService.findWelcomeCoupons()).willReturn(new CouponResponseWrapperDto(message, List.of(dto1, dto2), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/welcome"));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.coupons.size()").value(2));
    }

    @Test
    @Order(9)
    @DisplayName("쿠폰 다운로드")
    void enterCouponCode() throws Exception{

        //given
        Long couponMemberId = 1L;
        String couponCode = coupon1.getCode();
        String message = "쿠폰이 다운로드되었습니다! couponMemberId: " + couponMemberId;

        // couponService.enterCouponCode()가 couponMemberId 반환하게 설정
        given(couponService.enterCouponCode(couponCode)).willReturn(couponMemberId);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL)
                .content(couponCode));

        //then
        resultActions
                .andExpect(status().is(201))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(message));
    }

    @Test
    @Order(10)
    @DisplayName("쿠폰 다운로드 - 409에러 (등록 이력이 존재한 경우)")
    void enterCouponCode409Exception() throws Exception{

        //given
        String couponCode = "ERROR1";
        CustomException duplicateCouponException = new CustomException(ErrorCode.DUPLICATE_COUPON);
        String message = "쿠폰 등록 이력이 존재합니다.";

        // couponService.enterCouponCode()가 DUPLICATE_COUPON 에러 반환하게 설정
        given(couponService.enterCouponCode(couponCode)).willThrow(duplicateCouponException);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL)
                .content(couponCode));

        //then
        resultActions
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code").value(duplicateCouponException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    @Order(11)
    @DisplayName("쿠폰 사용")
    void redeemCoupon() throws Exception{

        //given
        Long couponMemberId = 1L;
        String message = "쿠폰이 사용되었습니다! couponMemberId: " + couponMemberId;

        // couponService.redeemCoupon()이 아무것도 안하게 설정
        willDoNothing().given(couponService).redeemCoupon(couponMemberId);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL + "/" + couponMemberId));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(message));
    }

    @Test
    @Order(12)
    @DisplayName("쿠폰 사용 내역 조회")
    void findOrder() throws Exception{

        //given
        Long orderId = 1L;
        Long couponId = coupon1.getId();

        // couponService.findOrder(couponId)가 orderId를 반환하게 설정
        given(couponService.findOrder(couponId)).willReturn(orderId);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/" + couponId + "/order"));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(orderId.toString()));
    }
}