package com.team2.fitinside.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BannerResponseDTO {
    private Long id;
    private String imageUrl;
    private Integer displayOrder;
    private String title;
    private Boolean isDeleted;
}
