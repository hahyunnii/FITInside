package com.team2.fitinside.product.service;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.mapper.ProductMapper;
import com.team2.fitinside.product.repository.ProductRepository;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 상품 목록 조회
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findByIsDeletedFalse().stream()
                .map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    // 상품 상세 조회
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return ProductMapper.INSTANCE.toDto(product);
    }

    // 상품 등록
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto productCreateDto) {
        Product product = ProductMapper.INSTANCE.toEntity(productCreateDto);

        // 카테고리 조회 및 설정
        Category category = categoryRepository.findById( productCreateDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDto(savedProduct);
    }

    // 상품 수정
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findById(productUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        ProductMapper.INSTANCE.toEntity(productUpdateDto); // DTO -> 엔티티 매핑

        // 카테고리 조회 및 설정
        Category category = categoryRepository.findById(productUpdateDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDto(updatedProduct);
    }

    // 상품 삭제 (soft delete)
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        product.setIsDeleted(true);
        productRepository.save(product);
    }
}
