package com.team2.fitinside.category.mapper;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.entity.Category;

public class CategoryMapper {

    // Category -> CategoryCreateRequestDTO 변환
    public static CategoryCreateRequestDTO toCreateDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryCreateRequestDTO.builder()
                .id(category.getId())  // ID를 포함하지 않는 경우, 제거 가능
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

    // Category -> CategoryUpdateRequestDTO 변환
    public static CategoryUpdateRequestDTO toUpdateDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryUpdateRequestDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

    // Category -> CategoryResponseDTO 변환
    public static CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

//    // DTO -> Category 변환 (Create용)
//    public static Category toEntityFromCreateDTO(CategoryCreateRequestDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        return Category.builder()
//                .name(dto.getName())
//                .displayOrder(dto.getDisplayOrder())
//                .isDeleted(dto.getIsDeleted())
//                // parentId를 통해 parent Category 설정 가능 (이 부분은 별도의 로직 필요)
//                .build();
//    }
//
//    // DTO -> Category 변환 (Update용)
//    public static Category toEntityFromUpdateDTO(CategoryUpdateRequestDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        return Category.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .displayOrder(dto.getDisplayOrder())
//                .isDeleted(dto.getIsDeleted())
//                // parentId를 통해 parent Category 설정 가능 (이 부분은 별도의 로직 필요)
//                .build();
//    }
}


