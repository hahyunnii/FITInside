package com.team2.fitinside.category.service;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.exception.CategoryAlreadyDeletedException;
import com.team2.fitinside.category.exception.CategoryResponseNotFoundException;
import com.team2.fitinside.category.mapper.CategoryMapper;
import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team2.fitinside.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import static com.team2.fitinside.category.mapper.CategoryMapper.toCreateDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 생성
    public CategoryCreateRequestDTO createCategory(CategoryCreateRequestDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .displayOrder(categoryDTO.getDisplayOrder())
                .parent(categoryDTO.getParentId() != null ?
                        categoryRepository.findById(categoryDTO.getParentId())
                                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
                        : null)
                .isDeleted(false)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return toCreateDTO(savedCategory);
    }

    //======================================================================
    // 카테고리 조회
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //======================================================================

    // 단일 카테고리 조회 메서드 (ID로 조회)
    public CategoryResponseDTO getCategoryResponse(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryResponseNotFoundException());

        if (category.getIsDeleted()) {
            throw new CategoryResponseNotFoundException();
        }

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDisplayOrder(),
                category.getIsDeleted(),
                category.getParent() != null ? category.getParent().getId() : null
        );
    }

    //=======================================================================

    // 카테고리 수정
    public CategoryUpdateRequestDTO updateCategory(Long id, CategoryUpdateRequestDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (category.getIsDeleted()) {
            throw new CategoryAlreadyDeletedException();
        }

        Category parentCategory = categoryDTO.getParentId() != null ?
                categoryRepository.findById(categoryDTO.getParentId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
                : null;

        category.updateCategory(categoryDTO.getName(), categoryDTO.getDisplayOrder(), parentCategory);

        return CategoryMapper.toUpdateDTO(categoryRepository.save(category));
    }


    //===========================================================================
    // 카테고리 삭제 (soft delete)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        category.delete(); // Soft delete
        categoryRepository.save(category);
    }
}