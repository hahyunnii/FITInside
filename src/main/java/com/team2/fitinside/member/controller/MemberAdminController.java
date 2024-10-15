package com.team2.fitinside.member.controller;


import com.team2.fitinside.member.dto.MemberListResponse;
import com.team2.fitinside.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/member")
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberListResponse> getMembers(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        MemberListResponse memberList = memberService.getMemberList(page);

        return ResponseEntity.ok(memberList);
    }
}
