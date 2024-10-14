package com.team2.fitinside.category.mapper;

import com.team2.fitinside.category.dto.CategoryImageResponseDTO;
import com.team2.fitinside.category.entity.CategoryImage;

public class CategoryImageMapper {

    // CategoryImage -> CategoryImageResponseDTO 변환
    public static CategoryImageResponseDTO toResponseDTO(CategoryImage categoryImage) {
        if (categoryImage == null) {
            return null;
        }

        return CategoryImageResponseDTO.builder()
                .id(categoryImage.getId())
                .imageUrl(categoryImage.getImageUrl())
                .categoryId(categoryImage.getCategory().getId())
                .build();
    }
}
