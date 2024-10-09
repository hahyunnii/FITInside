package com.team2.fitinside.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Where(clause = "is_deleted = false")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private boolean isDeleted;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) { this.password = password; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAuthority(Authority authority) { this.authority = authority; }

    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }


    @Builder
    public Member(Long id, String email, String password, String userName, String phone, Authority authority, boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.authority = authority;
        this.isDeleted = isDeleted;
    }
}