package com.team2.fitinside.member.oath.service;


import com.team2.fitinside.member.oath.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    public void saveOrUpdate(String name, String refreshToken, String accessToken) {

    }
}
