package com.team2.fitinside.category.controller;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
//import com.team2.fitinside.category.dto.CategoryDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryCreateRequestDTO> createCategory(@RequestBody CategoryCreateRequestDTO categoryDTO) {
        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }


    // 모든 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    // 카테고리 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequestDTO categoryDTO) {
        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }


    // 카테고리 삭제 (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}



