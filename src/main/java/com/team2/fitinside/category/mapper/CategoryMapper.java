package com.team2.fitinside.category.mapper;

import com.team2.fitinside.category.dto.CategoryDTO;
import com.team2.fitinside.category.entity.Category;

public class CategoryMapper{
    // Category -> CategoryDTO 변환
    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
//                .categoryImageUrl(category.getCategoryImage() != null ? category.getCategoryImage().getImageUrl() : null) // 카테고리 이미지 URL 추가
                .build();
    }
}

