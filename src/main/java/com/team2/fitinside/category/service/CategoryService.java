package com.team2.fitinside.category.service;

import com.team2.fitinside.category.mapper.CategoryMapper;
import com.team2.fitinside.category.dto.CategoryDTO;
import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.team2.fitinside.category.mapper.CategoryMapper.toDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 생성
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .displayOrder(categoryDTO.getDisplayOrder())
                .parent(categoryDTO.getParentId() != null ? categoryRepository.findById(categoryDTO.getParentId()).orElse(null) : null)
                .isDeleted(false)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return toDTO(savedCategory);
    }

    //======================================================================
    // 카테고리 조회
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    //=======================================================================
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Category parentCategory = categoryDTO.getParentId() != null ?
                categoryRepository.findById(categoryDTO.getParentId()).orElse(null) : null;

        // 명시적 메서드로 필드 업데이트
        category.updateCategory(categoryDTO.getName(), categoryDTO.getDisplayOrder(), parentCategory);

        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    //===========================================================================
    // 카테고리 삭제 (soft delete)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.delete(); // Soft delete
        categoryRepository.save(category);
    }

}





