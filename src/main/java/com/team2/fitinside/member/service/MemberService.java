package com.team2.fitinside.member.service;

import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.dto.MemberResponseDto;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.mapper.MemberMapper;
import com.team2.fitinside.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public MemberResponseDto getMyInfoBySecurity() {
        Member me = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return memberMapper.memberToResponse(me);
    }

    @Transactional
    public MemberResponseDto changeMemberUserName(String userName) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.setUserName(userName);
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPassword(String exPassword, String newPassword) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.AUTH_CODE_EXTENSION);
//            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.setPassword(passwordEncoder.encode((newPassword)));
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPhone(String phone) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.setPhone(phone);
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto deleteMember(){
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        memberRepository.delete(member);
        return memberMapper.memberToResponse(member);
    }
}