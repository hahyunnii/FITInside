package com.team2.fitinside.category.service;

import com.team2.fitinside.category.dto.CategoryDTO;
import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .build();

        Category savedCategory = categoryRepository.save(category);
        return toDTO(savedCategory);
    }

    // 카테고리 업데이트
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found");
        }

        Category category = categoryOpt.get();
        category.setName(categoryDTO.getName());
        category.setDisplayOrder(categoryDTO.getDisplayOrder());
        category.setParent(categoryDTO.getParentId() != null ? categoryRepository.findById(categoryDTO.getParentId()).orElse(null) : null);

        return toDTO(categoryRepository.save(category));
    }

    // 카테고리 삭제 (soft delete)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.delete(); // Soft delete
        categoryRepository.save(category);
    }

}
