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

    private String imageUrl;

    private Integer displayOrder;

    private String title;

    private Boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.ALL)
    private BannerImage bannerImage;


    // Soft delete 메서드
    public void setIsDeleted() {
        this.isDeleted = true;
    }
}


