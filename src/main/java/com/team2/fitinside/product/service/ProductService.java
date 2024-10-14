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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 상품 전체 목록 조회 (카테고리 필터 없이 모든 상품 반환)
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findByIsDeletedFalse().stream()
                .map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리 필터를 적용한 상품 목록 조회
    public List<ProductResponseDto> findAllProducts(Long categoryId) {
        return productRepository.findByIsDeletedFalse().stream()
                .filter(product -> categoryId == null || product.getCategory().getId().equals(categoryId))
                .map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
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
