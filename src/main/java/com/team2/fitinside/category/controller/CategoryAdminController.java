package com.team2.fitinside.category.controller;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
//import com.team2.fitinside.category.dto.CategoryImageResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@RestController
//@RequestMapping("/api/admin/categories")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class CategoryAdminController {
//
//    private final CategoryService categoryService;
//
//    // 카테고리 생성 (이미지 파일 포함)
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryCreateRequestDTO> createCategory(
//            @RequestParam("name") String name,
//            @RequestParam("displayOrder") Long displayOrder,
//            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
//            @RequestParam(value = "parentId", required = false) Long parentId,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        // 카테고리 생성
//        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(name, displayOrder, isDeleted, parentId, imageFile);
//        return ResponseEntity.ok(createdCategory);
//    }
//
//    // 카테고리 업데이트 (이미지 파일 포함)
//    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(
//            @PathVariable Long id,
//            @RequestParam("name") String name,
//            @RequestParam("displayOrder") Long displayOrder,
//            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
//            @RequestParam(value = "parentId", required = false) Long parentId,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        // 객체 생성
//        CategoryUpdateRequestDTO categoryDTO = CategoryUpdateRequestDTO.builder()
//                .name(name)
//                .displayOrder(displayOrder)
//                .isDeleted(isDeleted)
//                .parentId(parentId)
//                .build();
//
//        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO, imageFile);
//        return ResponseEntity.ok(updatedCategory);
//    }
//
//    // 카테고리 삭제 (soft delete)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
//    }
//}

//@RestController
//@RequestMapping("/api/admin/categories")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class CategoryAdminController {
//
//    private final CategoryService categoryService;
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryCreateRequestDTO> createCategory(
//            @RequestParam("name") String name,
//            @RequestParam("displayOrder") Long displayOrder,
//            @RequestParam(value = "mainDisplayOrder", required = false) Long mainDisplayOrder,
//            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
//            @RequestParam(value = "parentId", required = false) Long parentId,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(name, displayOrder, mainDisplayOrder, isDeleted, parentId, imageFile);
//        return ResponseEntity.ok(createdCategory);
//    }
//
//
//    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(
//            @PathVariable Long id,
//            @RequestParam("name") String name,
//            @RequestParam("displayOrder") Long displayOrder,
//            @RequestParam(value = "mainDisplayOrder", required = false) Long mainDisplayOrder,
//            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
//            @RequestParam(value = "parentId", required = false) Long parentId,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        // CategoryUpdateRequestDTO를 생성할 때 mainDisplayOrder가 null이면 기본값(null)으로 전달됩니다.
//        CategoryUpdateRequestDTO categoryDTO = CategoryUpdateRequestDTO.builder()
//                .name(name)
//                .displayOrder(displayOrder)
//                .mainDisplayOrder(mainDisplayOrder) // null이 전달될 수 있도록 그대로 설정
//                .isDeleted(isDeleted)
//                .parentId(parentId)
//                .build();
//
//        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO, imageFile);
//        return ResponseEntity.ok(updatedCategory);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
//    }
//}

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryAdminController {

    private final CategoryService categoryService;

    // 카테고리 생성 엔드포인트
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryCreateRequestDTO> createCategory(
            @RequestParam("name") String name,
            @RequestParam("displayOrder") Long displayOrder,
            @RequestParam(value = "mainDisplayOrder", required = false) Long mainDisplayOrder,
            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(name, displayOrder, mainDisplayOrder, isDeleted, parentId, imageFile);
        return ResponseEntity.ok(createdCategory);
    }

    // 카테고리 수정 엔드포인트
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("displayOrder") Long displayOrder,
            @RequestParam(value = "mainDisplayOrder", required = false) Long mainDisplayOrder,
            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        CategoryUpdateRequestDTO categoryDTO = CategoryUpdateRequestDTO.builder()
                .name(name)
                .displayOrder(displayOrder)
                .mainDisplayOrder(mainDisplayOrder)
                .isDeleted(isDeleted)
                .parentId(parentId)
                .build();

        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO, imageFile);
        return ResponseEntity.ok(updatedCategory);
    }

    // 카테고리 삭제 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }
}
