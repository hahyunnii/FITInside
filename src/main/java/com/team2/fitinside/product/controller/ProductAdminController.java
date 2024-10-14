package com.team2.fitinside.product.controller;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductInsertDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.image.S3ImageService;
import com.team2.fitinside.product.mapper.ProductMapper;
import com.team2.fitinside.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    private final ProductService productService;
    private final S3ImageService s3ImageService; // S3ImageService 의존성 주입

    // 생성자 주입
    @Autowired
    public ProductAdminController(ProductService productService, S3ImageService s3ImageService) {
        this.productService = productService;
        this.s3ImageService = s3ImageService; // 의존성 주입
    }

    // 상품 등록 (관리자 전용)
//    @PostMapping
//    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
//    @ApiResponse(responseCode = "201", description = "상품 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
//    @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음", content = @Content(mediaType = "application/json"))
//    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
//        ProductResponseDto createdProduct = productService.createProduct(productCreateDto);
//        return ResponseEntity.status(201).body(createdProduct);
//    }

//    @PostMapping(consumes = "multipart/form-data")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록하며 이미지를 함께 업로드합니다.")
    @ApiResponse(responseCode = "201", description = "상품 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> createProduct(
            @ModelAttribute("productData") ProductInsertDto productInsertDto) {

        // 이미지 업로드 처리 (S3 업로드 후 URL 생성)
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : productInsertDto.getProductImgUrls()) {
            String imageUrl = s3ImageService.upload(image); // 인스턴스 메서드 호출
            imageUrls.add(imageUrl);
        }

        // 매퍼를 사용하여 ProductInsertDto를 ProductCreateDto로 변환
        ProductCreateDto productCreateDto = ProductMapper.INSTANCE.toProductCreateDto(productInsertDto);

        // 이미지 URL 추가
        productCreateDto.setProductImgUrls(imageUrls);

        // 상품 등록 처리
        ProductResponseDto createdProduct = productService.createProduct(productCreateDto);
        return ResponseEntity.status(201).body(createdProduct);
    }



    // 상품 수정 (관리자 전용)
//    @PutMapping("/{id}")
//    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정합니다.")
//    @ApiResponse(responseCode = "200", description = "상품 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
//    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json"))
//    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto productUpdateDto) {
//        ProductResponseDto updatedProduct = productService.updateProduct(id, productUpdateDto);
//        return ResponseEntity.ok(updatedProduct);
//    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정합니다. 이미지를 함께 수정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @ModelAttribute ProductInsertDto productInsertDto) {

        // 이미지 업로드 처리 (S3 업로드 후 URL 생성)
        List<String> imageUrls = new ArrayList<>();
        List<MultipartFile> productImgUrls = productInsertDto.getProductImgUrls();

        // 매퍼를 사용하여 ProductInsertDto를 ProductUpdateDto로 변환
        ProductUpdateDto productUpdateDto = ProductMapper.INSTANCE.toProductUpdateDto(productInsertDto);

        if (productImgUrls != null && !productImgUrls.isEmpty()) {
            for (MultipartFile image : productImgUrls) {
                String imageUrl = s3ImageService.upload(image); // S3에 이미지 업로드
                imageUrls.add(imageUrl);
            }
            // 이미지 URL을 ProductUpdateDto에 설정
            productUpdateDto.setProductImgUrls(imageUrls);
        }

        // 상품 수정 처리
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
