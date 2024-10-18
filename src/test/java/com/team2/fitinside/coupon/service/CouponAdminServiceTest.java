package com.team2.fitinside.coupon.service;

import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.coupon.dto.CouponCreateRequestDto;
import com.team2.fitinside.coupon.dto.CouponEmailRequestDto;
import com.team2.fitinside.coupon.dto.CouponMemberResponseWrapperDto;
import com.team2.fitinside.coupon.dto.CouponResponseWrapperDto;
import com.team2.fitinside.coupon.entity.Coupon;
import com.team2.fitinside.coupon.entity.CouponMember;
import com.team2.fitinside.coupon.entity.CouponType;
import com.team2.fitinside.coupon.mapper.CouponMapper;
import com.team2.fitinside.coupon.repository.CouponMemberRepository;
import com.team2.fitinside.coupon.repository.CouponRepository;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.entity.Authority;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 관리자 서비스 단위 테스트")
class CouponAdminServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMemberRepository couponMemberRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private CouponEmailService couponEmailService;

    @InjectMocks
    private CouponAdminService couponAdminService;

    private Member adminMember;
    private Member userMember;

    private Coupon activeCoupon1;
    private Coupon activeCoupon2;
    private Coupon inActiveCoupon1;

    private CouponMember couponMember1;
    private CouponMember couponMember2;

    @BeforeEach
    void setUp() {

        // 테스트용 회원 객체 생성
        adminMember = Member.builder().id(1L).email("test@test.com").password("password1234").userName("관리자")
                .phone("010-1111-1111").authority(Authority.ROLE_ADMIN).build();

        userMember = Member.builder().id(2L).email("test22@test.com").password("password1234").userName("회원")
                .phone("010-1111-1111").authority(Authority.ROLE_USER).build();

        // 테스트용 쿠폰 객체 생성
        activeCoupon1 = Coupon.builder().id(1L).name("활성화 쿠폰 1").code("AAAAAA").value(10000).minValue(0).active(true).type(CouponType.AMOUNT).build();
        activeCoupon2 = Coupon.builder().id(2L).name("활성화 쿠폰 2").code("BBBBBB").percentage(20).minValue(20000).active(true).type(CouponType.AMOUNT).build();
        inActiveCoupon1 = Coupon.builder().id(3L).name("비활성화 쿠폰 1").code("CCCCCC").value(5000).minValue(30000).active(false).type(CouponType.AMOUNT).build();

        // 테스트용 쿠폰-멤버 객체 생성
        couponMember1 = CouponMember.builder().id(1L).coupon(activeCoupon1).member(userMember).used(false).build();
        couponMember1 = CouponMember.builder().id(2L).coupon(activeCoupon2).member(userMember).used(false).build();
    }

    @Test
    @DisplayName("유효기간이 지난 쿠폰 비활성화")
    public void testDeActiveCouponsByExpiredAt() {

        //given
        // 기한이 만료된 spy 쿠폰 생성
        Coupon spyCoupon1 = spy(Coupon.builder().expiredAt(LocalDate.now().minusDays(1)).build());
        Coupon spyCoupon2 = spy(Coupon.builder().expiredAt(LocalDate.now().minusDays(1)).build());

        // couponRepository.findByExpiredAtLessThanEqual 실행 시 coupon1, coupon2 리스트 반환하게 설정
        given(couponRepository.findByExpiredAtLessThanEqual(LocalDate.now())).willReturn(List.of(spyCoupon1, spyCoupon2));

        //when
        couponAdminService.deActiveCouponsByExpiredAt();

        //then
        verify(couponRepository, times(1)).findByExpiredAtLessThanEqual(LocalDate.now());
        verify(spyCoupon1, times(1)).deActive();
        verify(spyCoupon2, times(1)).deActive();
    }

    @Test
    @DisplayName("쿠폰 목록 조회 - 유효한 쿠폰만 조회")
    public void findAllActiveCoupons() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        int page = 1;
        boolean includeInActiveCoupons = false;

        // 활성화 쿠폰 반환하게 설정
        given(couponRepository.findByActiveIs(any(), eq(true))).willReturn(new PageImpl<>(List.of(activeCoupon1, activeCoupon2)));

        //when
        CouponResponseWrapperDto result = couponAdminService.findAllCoupons(page, includeInActiveCoupons);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 목록 조회 완료했습니다!");
        assertThat(result.getCoupons().size()).isEqualTo(2);
        assertThat(result.getCoupons().get(0).getName()).isEqualTo(activeCoupon1.getName());
        assertThat(result.getCoupons().get(1).getName()).isEqualTo(activeCoupon2.getName());
        assertThat(result.getTotalPages()).isEqualTo(1);

    }

    @Test
    @DisplayName("쿠폰 목록 조회 - 모든 쿠폰 조회")
    public void findAllCoupons() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        int page = 1;
        boolean includeInActiveCoupons = true;

        // 모든 쿠폰 반환하게 설정
        given(couponRepository.findAll((Pageable) any())).willReturn(new PageImpl<>(List.of(activeCoupon1, activeCoupon2, inActiveCoupon1)));

        //when
        CouponResponseWrapperDto result = couponAdminService.findAllCoupons(page, includeInActiveCoupons);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 목록 조회 완료했습니다!");
        assertThat(result.getCoupons().size()).isEqualTo(3);
        assertThat(result.getCoupons().get(0).getName()).isEqualTo(activeCoupon1.getName());
        assertThat(result.getCoupons().get(1).getName()).isEqualTo(activeCoupon2.getName());
        assertThat(result.getCoupons().get(2).getName()).isEqualTo(inActiveCoupon1.getName());
        assertThat(result.getCoupons().get(2).isActive()).isEqualTo(inActiveCoupon1.isActive());
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠폰 목록 조회 - 쿠폰이 없는 경우")
    public void findAllCouponsWhenCouponsEmpty() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        int page = 1;
        boolean includeInActiveCoupons = true;

        // 아무 쿠폰도 반환되지 않도록 설정
        given(couponRepository.findAll((Pageable) any())).willReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        //when
        CouponResponseWrapperDto result = couponAdminService.findAllCoupons(page, includeInActiveCoupons);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 목록 조회 완료했습니다!");
        assertThat(result.getCoupons().size()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠폰 보유한 회원 목록 조회")
    public void findCouponMembers() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        int page = 1;
        Long couponId = activeCoupon1.getId();

        // 쿠폰-멤버를 반환하게 설정
        given(couponMemberRepository.findByCoupon_Id(any(), eq(couponId))).willReturn(new PageImpl<>(List.of(couponMember1)));

        //when
        CouponMemberResponseWrapperDto result = couponAdminService.findCouponMembers(page, couponId);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 보유 회원 조회 완료했습니다!");
        assertThat(result.getMembers().size()).isEqualTo(1);
        assertThat(result.getMembers().get(0)).isEqualTo(CouponMapper.INSTANCE.toCouponMemberResponseDto(userMember));
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠폰 보유한 회원 목록 조회 - 회원이 없는 경우")
    public void findCouponMembersWhenMembersEmpty() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        int page = 1;
        Long couponId = activeCoupon1.getId();

        // 빈 객체 반환하게 설정
        given(couponMemberRepository.findByCoupon_Id(any(), eq(couponId))).willReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        //when
        CouponMemberResponseWrapperDto result = couponAdminService.findCouponMembers(page, couponId);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 보유 회원 조회 완료했습니다!");
        assertThat(result.getMembers().size()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠폰 생성")
    public void createCoupon() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        // 쿠폰 생성 dto
        CouponCreateRequestDto dto = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰").type(CouponType.AMOUNT).value(7000)
                .minValue(7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(1L).build();

        // 테스트용 카테고리
        Category category1 = Category.builder().id(1L).name("카테고리1").build();
        Coupon createdCoupon = Coupon.builder().id(4L).build();

        // 카테고리 반환하게 설정
        given(categoryRepository.findById(dto.getCategoryId())).willReturn(Optional.of(category1));
        given(couponRepository.save(any())).willReturn(createdCoupon);

        //when
        Long result = couponAdminService.createCoupon(dto);

        //then
        assertThat(result).isEqualTo(4L);

        // ArgumentCaptor 사용하여 save 메서드에서 전달된 Coupon 객체를 캡쳐
        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponRepository).save(couponCaptor.capture());

        // 캡쳐한 Coupon 객체의 setCategory가 호출되었는지 검증
        Coupon capturedCoupon = couponCaptor.getValue();
        assertNotNull(capturedCoupon);
        assertEquals(category1, capturedCoupon.getCategory()); // setCategory가 호출되었는지 검증
    }

    @Test
    @DisplayName("쿠폰 생성 - 400에러 (쿠폰 생성 정보가 유효하지 않은 경우)")
    public void createCouponWithInvalidData() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        // 유효하지 않은 쿠폰 생성 dto
        CouponCreateRequestDto dto1 = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰1").type(CouponType.AMOUNT).value(-7000)
                .minValue(7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(1L).build();
        CouponCreateRequestDto dto2 = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰2").type(CouponType.AMOUNT).percentage(-10)
                .minValue(7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(1L).build();
        CouponCreateRequestDto dto3 = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰3").type(CouponType.AMOUNT).percentage(200)
                .minValue(7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(1L).build();
        CouponCreateRequestDto dto4 = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰4").type(CouponType.AMOUNT).value(7000)
                .minValue(-7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(1L).build();
        CouponCreateRequestDto dto5 = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰5").type(CouponType.AMOUNT).value(7000)
                .minValue(7000).expiredAt(LocalDate.now().minusDays(3)).categoryId(1L).build();


        //when, then
        CustomException invalidCouponCreateData = assertThrows(CustomException.class, () ->
                couponAdminService.createCoupon(dto1));
        assertThat(invalidCouponCreateData.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_CREATE_DATA);

        assertThrows(CustomException.class, () -> couponAdminService.createCoupon(dto2));
        assertThrows(CustomException.class, () -> couponAdminService.createCoupon(dto3));
        assertThrows(CustomException.class, () -> couponAdminService.createCoupon(dto4));
        assertThrows(CustomException.class, () -> couponAdminService.createCoupon(dto5));
    }

    @Test
    @DisplayName("쿠폰 생성 - 카테고리 id가 0인 경우")
    public void createCouponWhenCategoryIdZero() throws Exception {

        //given
        // 관리자의 memberId 반환
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        // 쿠폰 생성 dto
        CouponCreateRequestDto dto = CouponCreateRequestDto.builder()
                .name("새로운 쿠폰").type(CouponType.AMOUNT).value(7000)
                .minValue(7000).expiredAt(LocalDate.now().plusDays(3)).categoryId(0L).build();

        Coupon createdCoupon = spy(Coupon.builder().id(4L).build());

        given(couponRepository.save(any())).willReturn(createdCoupon);

        //when
        Long result = couponAdminService.createCoupon(dto);

        //then
        assertThat(result).isEqualTo(4L);

        // setCategory() 가 호출되지 않음을 검증
        verify(createdCoupon, never()).setCategory(any());
    }

    @Test
    @DisplayName("쿠폰 비활성화")
    public void deActiveCoupon() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        Long couponId = activeCoupon1.getId();
        Coupon spycoupon = spy(activeCoupon1);
        given(couponRepository.findById(1L)).willReturn(Optional.of(spycoupon));

        //when
        Long result = couponAdminService.deActiveCoupon(couponId);

        //then
        assertThat(result).isEqualTo(couponId);
        verify(spycoupon, times(1)).deActive();
    }

    @Test
    @DisplayName("쿠폰 비활성화 - 쿠폰을 찾을 수 없는 경우")
    public void deActiveCouponWhenCouponNotFound() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        Long couponId = 4L;
        given(couponRepository.findById(4L)).willReturn(Optional.empty());

        //when, then
        CustomException couponNotFoundException = assertThrows(CustomException.class, () -> couponAdminService.deActiveCoupon(couponId));
        assertThat(couponNotFoundException.getErrorCode()).isEqualTo(ErrorCode.COUPON_NOT_FOUND);
    }

    @Test
    @DisplayName("쿠폰 이메일 전송")
    public void sendEmail() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        Long couponId = activeCoupon1.getId();
        given(couponRepository.findById(couponId)).willReturn(Optional.of(activeCoupon1));
        willDoNothing().given(couponEmailService).sendEmail(any());

        // 쿠폰 이메일 요청 dto
        CouponEmailRequestDto dto = new CouponEmailRequestDto(couponId, userMember.getEmail(), "이메일 템플릿");

        //when
        String emailAddress = couponAdminService.sendEmail(dto);

        //then
        assertThat(emailAddress).isEqualTo(userMember.getEmail());
        verify(couponEmailService, times(1)).sendEmail(dto);
    }

    @Test
    @DisplayName("쿠폰 이메일 전송 - 비활성화된 쿠폰인 경우")
    public void sendEmailWhenCouponIsInActive() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        Long couponId = inActiveCoupon1.getId();
        given(couponRepository.findById(couponId)).willReturn(Optional.of(inActiveCoupon1));

        // 쿠폰 이메일 요청 dto
        CouponEmailRequestDto dto = new CouponEmailRequestDto(couponId, userMember.getEmail(), "이메일 템플릿");

        //when, then
        CustomException invalidCouponDataException = assertThrows(CustomException.class,
                () -> couponAdminService.sendEmail(dto));
        assertThat(invalidCouponDataException.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_DATA);
    }

    @Test
    @DisplayName("쿠폰 미보유 회원 목록 조회")
    public void findMembersWithOutCoupons() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(adminMember.getId());
        given(memberRepository.findById(adminMember.getId())).willReturn(Optional.of(adminMember));

        Long couponId = activeCoupon1.getId();
        given(couponRepository.findById(couponId)).willReturn(Optional.of(activeCoupon1));
        given(memberRepository.findAll()).willReturn(List.of(adminMember, userMember));

        given(couponMemberRepository.existsByMember_IdAndCoupon_Id(userMember.getId(), couponId)).willReturn(true);
        given(couponMemberRepository.existsByMember_IdAndCoupon_Id(adminMember.getId(), couponId)).willReturn(false);

        //when
        CouponMemberResponseWrapperDto result = couponAdminService.findMembersWithOutCoupons(couponId);

        //then
        assertThat(result.getMessage()).isEqualTo("쿠폰 미보유 회원 목록을 조회했습니다!");
        assertThat(result.getMembers().size()).isEqualTo(1);
        assertThat(result.getMembers().get(0).getUserName()).isEqualTo(adminMember.getUserName());
        assertThat(result.getMembers().get(0).getEmail()).isEqualTo(adminMember.getEmail());

    }

    @Test
    @DisplayName("관리자 권한이 없는 경우")
    public void checkAdminFail() throws Exception {

        //given
        given(securityUtil.getCurrentMemberId()).willReturn(userMember.getId());
        given(memberRepository.findById(userMember.getId())).willReturn(Optional.of(userMember));

        //when, then
        CustomException userNotAuthorizedException = assertThrows(CustomException.class,
                () -> couponAdminService.deActiveCoupon(1L));
        assertThat(userNotAuthorizedException.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_AUTHORIZED);


    }
}