package com.team2.fitinside.product.controller;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "게시글 추가", description = "새로운 상품을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "상품 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCreateDto.class)))
    @ApiResponse(responseCode = "401", description = "카테고리가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "DB 연결 에러", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        ProductResponseDto createdProduct = productService.createProduct(productCreateDto);
        return ResponseEntity.ok(createdProduct);
    }

    // 상품 수정 (관리자 전용)
    @PutMapping("/admin/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto productUpdateDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productUpdateDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제 (관리자 전용, soft delete)
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long id) {
        ProductResponseDto deletedProduct = productService.deleteProduct(id);
        return ResponseEntity.ok(deletedProduct);
    }
}
