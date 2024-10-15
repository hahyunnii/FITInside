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

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryAdminController {

    private final CategoryService categoryService;

//    // 카테고리 생성
//    @PostMapping
//    public ResponseEntity<CategoryCreateRequestDTO> createCategory(@RequestBody CategoryCreateRequestDTO categoryDTO) {
//        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(categoryDTO);
//        return ResponseEntity.ok(createdCategory);
//    }

    // 카테고리 생성 (이미지 파일 포함)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryCreateRequestDTO> createCategory(
            @RequestParam("name") String name,
            @RequestParam("displayOrder") Long displayOrder,
            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        CategoryCreateRequestDTO createdCategory = categoryService.createCategory(name, displayOrder, isDeleted, parentId, imageFile);
        return ResponseEntity.ok(createdCategory);
    }


//    // 카테고리 업데이트
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequestDTO categoryDTO) {
//        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
//        return ResponseEntity.ok(updatedCategory);
//    }

//    // 카테고리 업데이트 (이미지 파일 포함)
//    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(
//            @PathVariable Long id,
//            @RequestPart("categoryDTO") @Valid CategoryUpdateRequestDTO categoryDTO,
//            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO, imageFile);
//        return ResponseEntity.ok(updatedCategory);
//    }

    // 카테고리 업데이트 (이미지 파일 포함)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryUpdateRequestDTO> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("displayOrder") Long displayOrder,
            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        CategoryUpdateRequestDTO categoryDTO = CategoryUpdateRequestDTO.builder()
                .name(name)
                .displayOrder(displayOrder)
                .isDeleted(isDeleted)
                .parentId(parentId)
                .build();

        CategoryUpdateRequestDTO updatedCategory = categoryService.updateCategory(id, categoryDTO, imageFile);
        return ResponseEntity.ok(updatedCategory);
    }


    // 카테고리 삭제 (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }
//
//    @Operation(summary = "Upload category image", description = "Upload an image for a category")
//    @ApiResponse(responseCode = "200", description = "Successful upload")
//    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CategoryImageResponseDTO> uploadCategoryImage(
//            @PathVariable Long id,
//            @Parameter(description = "Image file", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
//            @RequestParam("image") MultipartFile image) {
//        CategoryImageResponseDTO uploadedImage = categoryService.uploadCategoryImage(id, image);
//        return ResponseEntity.ok(uploadedImage);
//    }
//
//
//
//    // 카테고리 이미지 삭제
//    @DeleteMapping("/{id}/image")
//    public ResponseEntity<String> deleteCategoryImage(@PathVariable Long id) {
//        categoryService.deleteCategoryImage(id);
//        return ResponseEntity.ok("카테고리 이미지가 삭제되었습니다.");
//    }
}
