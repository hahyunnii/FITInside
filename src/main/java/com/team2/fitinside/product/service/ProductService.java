package com.team2.fitinside.product.service;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.product.mapper.ProductMapper;
import com.team2.fitinside.product.repository.ProductRepository;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 페이지네이션, 정렬, 검색을 적용한 상품 전체 목록 조회
    public Page<ProductResponseDto> getAllProducts(int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.searchByKeywordAndIsDeletedFalse(keyword, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        } else {
            return productRepository.findByIsDeletedFalse(pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        }
    }

    // 페이지네이션, 정렬, 검색을 적용한 카테고리별 상품 목록 조회
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.searchByKeywordAndCategoryAndIsDeletedFalse(category, keyword, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        } else {
            return productRepository.findByCategoryAndIsDeletedFalse(category, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        }
    }

    // 상품 상세 조회
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductMapper.INSTANCE.toDto(product);
    }

    // 상품 등록
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto productCreateDto) {
        Product product = ProductMapper.INSTANCE.toEntity(productCreateDto);

        Category category = categoryRepository.findById(productCreateDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDto(savedProduct);
    }

    // 상품 수정
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateDto productUpdateDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Product updatedProduct = ProductMapper.INSTANCE.toEntity(id, productUpdateDto);

        Category category = categoryRepository.findById(productUpdateDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        updatedProduct.setCategory(category);

        productRepository.save(updatedProduct);
        return ProductMapper.INSTANCE.toDto(updatedProduct);
    }

    // 상품 삭제 (soft delete)
    @Transactional
    public ProductResponseDto deleteProduct(Long id) {
        Product deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        deletedProduct.setIsDeleted(true);
        productRepository.save(deletedProduct);
        return ProductMapper.INSTANCE.toDto(deletedProduct);
    }
}
