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

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    // 상품 등록 (관리자 전용)
    @PostMapping
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "상품 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        ProductResponseDto createdProduct = productService.createProduct(productCreateDto);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 상품 수정 (관리자 전용)
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto productUpdateDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productUpdateDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제 (관리자 전용, soft delete)
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다. (soft delete)")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long id) {
        ProductResponseDto deletedProduct = productService.deleteProduct(id);
        return ResponseEntity.ok(deletedProduct);
    }
}
