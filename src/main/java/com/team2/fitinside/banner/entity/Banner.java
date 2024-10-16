package com.team2.fitinside.banner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지 URL 필드 추가
    private String imageUrl;

    private Integer displayOrder;

    private String title;

    private Boolean isDeleted = false;

    // Soft delete 메서드
    public void setIsDeleted() {
        this.isDeleted = true;
    }

    public Banner updateDetails(String title, String imageUrl, Integer displayOrder) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        return this;
    }

    // 삭제 시 isDeleted를 true로 설정하는 메서드
    public void delete() {
        this.isDeleted = true;
    }
}


