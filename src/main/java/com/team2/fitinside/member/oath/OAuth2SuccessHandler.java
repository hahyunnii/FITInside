package com.team2.fitinside.member.oath;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.jwt.TokenProvider;
import com.team2.fitinside.member.oath.entity.RefreshToken;
import com.team2.fitinside.member.oath.repository.RefreshTokenRepository;
import com.team2.fitinside.member.oath.util.CookieUtil;
import com.team2.fitinside.member.repository.MemberRepository;
import com.team2.fitinside.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.team2.fitinside.member.jwt.TokenProvider.REFRESH_TOKEN_EXPIRE_TIME;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String URI = "http://localhost:3000/tokenCheck";
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); // 인증된 사용자의 정보를 가져옴
        Member member = memberRepository.findByEmail((String) oAuth2User.getAttributes().get("email"))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 정보 조회

        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);

        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        saveRefreshToken(member.getId(), refreshToken); // 리프레시 토큰 저장
        addRefreshTokenToCookie(request, response, refreshToken); // 리프레시 토큰을 쿠키에 추가


        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);

    }

    // 리프레시 토큰을 DB에 저장하는 메서드
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(userId)
                .map(entity -> entity.update(newRefreshToken))      // 기존 토큰이 있으면 업데이트
                .orElse(new RefreshToken(userId, newRefreshToken)); // 없으면 새로 생성

        refreshTokenRepository.save(refreshToken); // 저장소에 리프레시 토큰 저장
    }

    // 리프레시 토큰을 쿠키에 추가하는 메서드
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_EXPIRE_TIME; // 쿠키 유효 기간 설정

        CookieUtil.deleteCookie(request, response, "refreshToken"); // 기존 쿠키 삭제
        CookieUtil.addCookie(response, "refreshToken", refreshToken, cookieMaxAge); // 새 쿠키 추가
    }
}
