package com.team2.fitinside.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.fitinside.coupon.dto.*;
import com.team2.fitinside.coupon.entity.Coupon;
import com.team2.fitinside.coupon.entity.CouponType;
import com.team2.fitinside.coupon.mapper.CouponMapper;
import com.team2.fitinside.coupon.service.CouponAdminService;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CouponAdminController.class)
@AutoConfigureMockMvc(addFilters = false) // 필터 제외 (JWT 검증 제외)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("쿠폰 관리자 컨트롤러 단위 테스트")
class CouponAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponAdminService couponAdminService;

    // 기본 장바구니 컨트롤러의 url
    private static final String URL = "/api/admin/coupons";

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

        // couponAdminService.findAllCoupons() 호출 시 CouponResponseWrapperDto 반환하게 설정
        given(couponAdminService.findAllCoupons(PAGE, includeInActiveCoupons))
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

//        String expectedJson = objectMapper.writeValueAsString(new CouponResponseWrapperDto("쿠폰 목록 조회 완료했습니다!", List.of(dto1, dto2), 1));
//        String actualJson = resultActions.andReturn().getResponse().getContentAsString();
//
//        assertThat(actualJson).isEqualTo(expectedJson); // 전체 JSON 비교
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

        // couponAdminService.findAllCoupons() 호출 시 CouponResponseWrapperDto 반환하게 설정
        given(couponAdminService.findAllCoupons(PAGE, includeInActiveCoupons))
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
        given(couponAdminService.findAllCoupons(PAGE, false))
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
    @DisplayName("특정 쿠폰 보유 회원 목록 조회")
    void findCouponMembers() throws Exception{

        //given
        Long couponId = coupon1.getId();
        String message = "쿠폰 보유 회원 목록 조회 완료했습니다!";
        // 테스트용 CouponMemberResponseDto 생성 (2개)
        CouponMemberResponseDto dto1 = CouponMemberResponseDto.builder().email("email1@test.com").userName("user1").build();
        CouponMemberResponseDto dto2 = CouponMemberResponseDto.builder().email("email2@test.com").userName("user2").build();
        // couponAdminService.findCouponMembers가 CouponMemberResponseWrapperDto를 반환하게 설정
        given(couponAdminService.findCouponMembers(PAGE, couponId))
                .willReturn(new CouponMemberResponseWrapperDto(message, List.of(dto1, dto2), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/" + couponId));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.members.length()").value(2))
                .andExpect(jsonPath("$.members[0].email").value(dto1.getEmail()))
                .andExpect(jsonPath("$.members[0].userName").value(dto1.getUserName()))
                .andExpect(jsonPath("$.members[0].email").value(dto1.getEmail()))
                .andExpect(jsonPath("$.members[0].userName").value(dto1.getUserName()));
    }

    @Test
    @Order(5)
    @DisplayName("쿠폰 생성")
    void createCoupon() throws Exception {

        //given
        // CouponCreateRequestDto 생성
        CouponCreateRequestDto dto = CouponCreateRequestDto.builder()
                .name("coupon3").type(CouponType.AMOUNT).value(5000)
                .minValue(70000).expiredAt(LocalDate.of(2025, 1, 1)).categoryId(0L).build();

        given(couponAdminService.createCoupon(dto)).willReturn(3L);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        resultActions
                .andExpect(status().is(201))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("쿠폰이 생성되었습니다! couponId: 3"));
    }

    @Test
    @Order(6)
    @DisplayName("쿠폰 생성 - 400에러 (쿠폰 생성 정보가 유효하지 않는 경우)")
    void createCoupon400Exception() throws Exception {

        //given
        CustomException invalidCouponException = new CustomException(ErrorCode.INVALID_COUPON_CREATE_DATA);

        CouponCreateRequestDto dto = CouponCreateRequestDto.builder()
                .name("coupon3").type(CouponType.AMOUNT).value(5000)
                .minValue(70000).expiredAt(LocalDate.of(2025, 1, 1)).categoryId(0L).build();

        given(couponAdminService.createCoupon(any())).willThrow(invalidCouponException);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        resultActions
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(invalidCouponException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("쿠폰 생성 정보가 유효하지 않습니다."));
    }

    @Test
    @Order(7)
    @DisplayName("쿠폰 비활성화")
    void deActiveCoupon() throws Exception {

        //given
        Long couponId = coupon1.getId();
        given(couponAdminService.deActiveCoupon(couponId)).willReturn(couponId);

        //when
        ResultActions resultActions = mockMvc.perform(delete(URL + "/" + couponId));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("쿠폰이 비활성화 되었습니다! couponId: " + couponId));
    }

    @Test
    @Order(8)
    @DisplayName("쿠폰 비활성화 - 404에러 (쿠폰을 찾을 수 없는 경우)")
    void deActiveCoupon404Exception() throws Exception {

        //given
        Long couponId = coupon1.getId();
        CustomException couponNotFoundException = new CustomException(ErrorCode.COUPON_NOT_FOUND);
        given(couponAdminService.deActiveCoupon(couponId)).willThrow(couponNotFoundException);

        //when
        ResultActions resultActions = mockMvc.perform(delete(URL + "/" + couponId));

        //then
        resultActions
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.code").value(couponNotFoundException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("해당 쿠폰을 찾을 수 없습니다."));
    }

    @Test
    @Order(9)
    @DisplayName("쿠폰 이메일 전송")
    void sendCouponEmails() throws Exception {

        //given
        Long couponId = coupon1.getId();
        String address = "test1@test.com";
        String template = "email template";
        CouponEmailRequestDto dto = new CouponEmailRequestDto(couponId, address, template);
        given(couponAdminService.sendEmail(dto)).willReturn(address);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("쿠폰 이메일 전송을 완료했습니다! email: " + address));
    }

    @Test
    @Order(10)
    @DisplayName("쿠폰 이메일 전송 - 400에러 (이메일 정보가 유효하지 않은 경우)")
    void sendCouponEmails400Exception() throws Exception {

        //given
        Long couponId = coupon1.getId();
        String address = "test1@test.com";
        String template = "email template";
        CustomException invalidEmailException = new CustomException(ErrorCode.INVALID_EMAIL_DATA);
        CouponEmailRequestDto dto = new CouponEmailRequestDto(couponId, address, template);
        given(couponAdminService.sendEmail(dto)).willThrow(invalidEmailException);

        //when
        ResultActions resultActions = mockMvc.perform(post(URL + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        resultActions
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(invalidEmailException.getErrorCode().toString()))
                .andExpect(jsonPath("$.message").value("이메일 정보가 유효하지 않습니다."));
    }

    @Test
    @Order(11)
    @DisplayName("특정 쿠폰 미보유 회원 조회")
    void findMembersWithOutCoupons() throws Exception {

        //given
        Long couponId = 3L;
        CouponMemberResponseDto dto1 = CouponMemberResponseDto.builder().email("email1@test.com").userName("user1").build();
        CouponMemberResponseDto dto2 = CouponMemberResponseDto.builder().email("email2@test.com").userName("user2").build();

        String message= "쿠폰 미보유 회원 목록을 조회했습니다!";

        // couponAdminService.findCouponMembers가 CouponMemberResponseWrapperDto를 반환하게 설정
        given(couponAdminService.findMembersWithOutCoupons(couponId))
                .willReturn(new CouponMemberResponseWrapperDto(message, List.of(dto1, dto2), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/" + couponId + "/members"));

        //then
        resultActions
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.members.length()").value(2))
                .andExpect(jsonPath("$.members[0].email").value(dto1.getEmail()))
                .andExpect(jsonPath("$.members[0].userName").value(dto1.getUserName()))
                .andExpect(jsonPath("$.members[0].email").value(dto1.getEmail()))
                .andExpect(jsonPath("$.members[0].userName").value(dto1.getUserName()));
    }
}