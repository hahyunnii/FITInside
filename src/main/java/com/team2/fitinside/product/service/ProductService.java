package com.team2.fitinside.product.service;

import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 모든 상품 조회
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    // 모든 상품 목록 조회
    public List<Product> getAllProducts(Integer categoryId) {
        if (categoryId != null) {
            return productRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
        } else {
            return productRepository.findByIsDeletedFalse();
        }
    }

    // 특정 상품 상세 조회
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // 상품 등록 (관리자 전용)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 상품 수정 (관리자 전용)
    public Optional<Product> updateProduct(Long productId, Product productDetails) {
        return productRepository.findById(productId).map(product -> {
            product.setProductName(productDetails.getProductName());
            product.setProductType(productDetails.getProductType());
            product.setPrice(productDetails.getPrice());
            product.setInfo(productDetails.getInfo());
            product.setManufacturer(productDetails.getManufacturer());
            product.setCategoryId(productDetails.getCategoryId());
            product.setThumbnails(productDetails.getThumbnails());
            return productRepository.save(product);
        });
    }

    // 상품 삭제 (관리자 전용)
//    public void deleteProduct(Long productId) {
//        productRepository.deleteById(productId);
//    }

    // 상품 삭제 (soft delete)
    public Optional<Product> deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setDeletedAt(LocalDateTime.now());
            return productRepository.save(product);
        });
    }
}
