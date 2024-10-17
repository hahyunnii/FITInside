package com.team2.fitinside.category.service;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;

import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
//import com.team2.fitinside.category.entity.CategoryImage;
import com.team2.fitinside.category.exception.CategoryAlreadyDeletedException;
import com.team2.fitinside.category.exception.CategoryResponseNotFoundException;
//import com.team2.fitinside.category.mapper.CategoryImageMapper;
import com.team2.fitinside.category.mapper.CategoryMapper;
import com.team2.fitinside.category.entity.Category;
//import com.team2.fitinside.category.repository.CategoryImageRepository;
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

//@Service
//@RequiredArgsConstructor
//@Transactional
//public class CategoryService {
//
//    private final CategoryRepository categoryRepository;
//    private final S3ImageService s3ImageService;
//
//    public CategoryCreateRequestDTO createCategory(String name, Long displayOrder, Boolean isDeleted, Long parentId, MultipartFile imageFile) {
//        String imageUrl = uploadImageToS3(imageFile);
//        // 부모 카테고리 조회
//        Category parentCategory = getParentCategory(parentId);
//
//        // 카테고리 객체 생성
//        Category category = Category.builder()
//                .name(name)
//                .displayOrder(displayOrder)
//                .parent(parentCategory)
//                .isDeleted(isDeleted != null ? isDeleted : false) // null 처리
//                .imageUrl(imageUrl)
//                .build();
//
//        Category savedCategory = categoryRepository.save(category);
//        return CategoryMapper.toCreateDTO(savedCategory);
//    }
//
//    // 이미지 업로드 메서드
//    private String uploadImageToS3(MultipartFile imageFile) {
//        if (imageFile != null && !imageFile.isEmpty()) {
//            return s3ImageService.upload(imageFile);
//        }
//        return null; // 이미지가 없을 경우 null 반환
//    }
//
//    // 부모 카테고리 조회 메서드
//    private Category getParentCategory(Long parentId) {
//        if (parentId != null) {
//            return categoryRepository.findById(parentId)
//                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        }
//        return null; // 부모 카테고리가 없을 경우 null 반환
//    }
//
//    //======================================================================
//    // 카테고리 조회
//    public List<CategoryResponseDTO> getAllCategories() {
//        return categoryRepository.findAllByIsDeletedFalse()
//                .stream()
//                .map(CategoryMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    //======================================================================
//
//    // 단일 카테고리 조회 메서드 (ID로 조회)
//    public CategoryResponseDTO getCategoryById(Long id) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CategoryResponseNotFoundException());
//
//        if (category.getIsDeleted()) {
//            throw new CategoryResponseNotFoundException();
//        }
//
//        return CategoryMapper.toResponseDTO(category); // Mapper로 응답 DTO 변환
//    }
//
//    //=======================================================================
//
//    // 카테고리 수정
//    public CategoryUpdateRequestDTO updateCategory(Long id, CategoryUpdateRequestDTO categoryDTO, MultipartFile imageFile) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//
//        if (category.getIsDeleted()) {
//            throw new CategoryAlreadyDeletedException();
//        }
//
//        // 부모 카테고리 조회
//        Category parentCategory = categoryDTO.getParentId() != null ?
//                categoryRepository.findById(categoryDTO.getParentId())
//                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
//                : null;
//
//        String imageUrl = category.getImageUrl();
//        if (imageFile != null && !imageFile.isEmpty()) {
//            if (imageUrl != null) {
//                s3ImageService.deleteImageFromS3(imageUrl); // 기존 이미지 삭제
//            }
//            imageUrl = s3ImageService.upload(imageFile); // 새 이미지 업로드
//        }
//
//        category.updateCategory(categoryDTO.getName(), categoryDTO.getDisplayOrder(), parentCategory, imageUrl);
//        return CategoryMapper.toUpdateDTO(categoryRepository.save(category));
//    }
//
//    //===========================================================================
//    // 카테고리 삭제 (soft delete)
//    public void deleteCategory(Long id) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        category.delete(); // Soft delete
//        categoryRepository.save(category);
//    }
//}

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3ImageService s3ImageService;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getParentCategories() {
        return categoryRepository.findAllByIsDeletedFalseAndParentIsNullOrderByDisplayOrder()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getChildCategories(Long parentId) {
        return categoryRepository.findAllByIsDeletedFalseAndParentIdOrderByDisplayOrder(parentId)
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryMapper.toResponseDTO(category);
    }

    public CategoryCreateRequestDTO createCategory(String name, Long displayOrder, Boolean isDeleted, Long parentId, MultipartFile imageFile) {
        if (parentId == null) {
            categoryRepository.incrementDisplayOrderForParentCategories(displayOrder, Long.MAX_VALUE);
        } else {
            categoryRepository.incrementDisplayOrderForChildCategories(displayOrder, Long.MAX_VALUE, parentId);
        }

        String imageUrl = uploadImageToS3(imageFile);
        Category parentCategory = getParentCategory(parentId);

        Category category = Category.builder()
                .name(name)
                .displayOrder(displayOrder)
                .parent(parentCategory)
                .isDeleted(isDeleted != null ? isDeleted : false)
                .imageUrl(imageUrl)
                .build();

        return CategoryMapper.toCreateDTO(categoryRepository.save(category));
    }

    public CategoryUpdateRequestDTO updateCategory(Long id, CategoryUpdateRequestDTO categoryDTO, MultipartFile imageFile) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Long oldDisplayOrder = category.getDisplayOrder();
        Long newDisplayOrder = categoryDTO.getDisplayOrder();

        if (!oldDisplayOrder.equals(newDisplayOrder)) {
            if (category.getParent() == null) {
                adjustDisplayOrderForParentCategories(oldDisplayOrder, newDisplayOrder);
            } else {
                adjustDisplayOrderForChildCategories(oldDisplayOrder, newDisplayOrder, category.getParent().getId());
            }
        }

        String imageUrl = updateCategoryImage(category, imageFile);
        category.updateCategory(categoryDTO.getName(), newDisplayOrder, getParentCategory(categoryDTO.getParentId()), imageUrl);

        return CategoryMapper.toUpdateDTO(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (category.getParent() == null) {
            categoryRepository.decrementDisplayOrderForParentCategories(category.getDisplayOrder(), Long.MAX_VALUE);
        } else {
            categoryRepository.decrementDisplayOrderForChildCategories(category.getDisplayOrder(), Long.MAX_VALUE, category.getParent().getId());
        }

        category.delete();
    }

    private void adjustDisplayOrderForParentCategories(Long oldOrder, Long newOrder) {
        if (newOrder > oldOrder) {
            categoryRepository.decrementDisplayOrderForParentCategories(oldOrder + 1, newOrder);
        } else {
            categoryRepository.incrementDisplayOrderForParentCategories(newOrder, oldOrder - 1);
        }
    }

    private void adjustDisplayOrderForChildCategories(Long oldOrder, Long newOrder, Long parentId) {
        if (newOrder > oldOrder) {
            categoryRepository.decrementDisplayOrderForChildCategories(oldOrder + 1, newOrder, parentId);
        } else {
            categoryRepository.incrementDisplayOrderForChildCategories(newOrder, oldOrder - 1, parentId);
        }
    }

    private Category getParentCategory(Long parentId) {
        return parentId == null ? null : categoryRepository.findByIdAndIsDeletedFalse(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private String uploadImageToS3(MultipartFile imageFile) {
        return (imageFile != null && !imageFile.isEmpty()) ? s3ImageService.upload(imageFile) : null;
    }

    private String updateCategoryImage(Category category, MultipartFile imageFile) {
        String imageUrl = category.getImageUrl();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (imageUrl != null) {
                s3ImageService.deleteImageFromS3(imageUrl);
            }
            imageUrl = s3ImageService.upload(imageFile);
        }
        return imageUrl;
    }
}

