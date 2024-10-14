package com.team2.fitinside.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryImageResponseDTO {
    private Long id;
    private String imageUrl;
    private Long categoryId;  // Category ID를 참조
}
