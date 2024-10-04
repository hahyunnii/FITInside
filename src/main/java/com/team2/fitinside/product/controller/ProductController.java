package com.team2.fitinside.product.controller;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        ProductResponseDto product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    // 상품 등록 (관리자 전용)
    @PostMapping("/admin")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        ProductResponseDto createdProduct = productService.createProduct(productCreateDto);
        return ResponseEntity.ok(createdProduct);
    }

    // 상품 수정 (관리자 전용)
    @PutMapping("/admin/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto productUpdateDto) {
        productUpdateDto.setId(id); // PathVariable을 DTO에 반영
        ProductResponseDto updatedProduct = productService.updateProduct(productUpdateDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제 (관리자 전용, soft delete)
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }
}
