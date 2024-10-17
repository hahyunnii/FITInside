package com.team2.fitinside.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private long id;
    private String email;
    private String userName;
    private String phone;

}
