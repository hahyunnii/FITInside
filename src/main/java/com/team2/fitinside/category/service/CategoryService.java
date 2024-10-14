package com.team2.fitinside.category.service;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
import com.team2.fitinside.category.dto.CategoryImageResponseDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.entity.CategoryImage;
import com.team2.fitinside.category.exception.CategoryAlreadyDeletedException;
import com.team2.fitinside.category.exception.CategoryResponseNotFoundException;
import com.team2.fitinside.category.mapper.CategoryImageMapper;
import com.team2.fitinside.category.mapper.CategoryMapper;
import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryImageRepository;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.product.image.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team2.fitinside.global.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.team2.fitinside.category.mapper.CategoryMapper.toCreateDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryImageRepository categoryImageRepository;
    private final S3ImageService s3ImageService;

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

    //=====================================================================
    // 카테고리 이미지 업로드
    public CategoryImageResponseDTO uploadCategoryImage(Long categoryId, MultipartFile image) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 이미지 파일을 S3에 업로드하고 URL을 저장
        String imageUrl = s3ImageService.upload(image);

        // 기존 이미지가 있다면 삭제
        categoryImageRepository.findByCategory(category).ifPresent(categoryImageRepository::delete);

        // 새로운 이미지 저장
        CategoryImage categoryImage = CategoryImage.builder()
                .imageUrl(imageUrl)
                .category(category)
                .build();
        categoryImageRepository.save(categoryImage);

        return CategoryImageMapper.toResponseDTO(categoryImage);
    }

    //======================================================================
    // 카테고리 이미지 조회
    public Optional<CategoryImageResponseDTO> getCategoryImage(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryImageRepository.findByCategory(category)
                .map(CategoryImageMapper::toResponseDTO);
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
    public CategoryResponseDTO getCategoryById(Long id) {
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

    // 카테고리 이미지 삭제
    public void deleteCategoryImage(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 카테고리 이미지 조회 부분에서 예외 처리 주석 처리
        CategoryImage categoryImage = categoryImageRepository.findByCategory(category)
                // .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_IMAGE_NOT_FOUND));
                .orElse(null); // 예외 처리 없이 null을 반환

        // 이미지가 없으면 메서드를 종료
        if (categoryImage == null) {
            return;
        }

        // S3에서 이미지 삭제
        s3ImageService.deleteImageFromS3(categoryImage.getImageUrl());

        // 데이터베이스에서 이미지 삭제
        categoryImageRepository.delete(categoryImage);
    }
}

//--------------

