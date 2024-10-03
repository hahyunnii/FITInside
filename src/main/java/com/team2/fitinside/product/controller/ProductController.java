package com.team2.fitinside.product.controller;

import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 목록 조회
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "category_id", required = false) Integer categoryId) {
        List<Product> products = productService.getAllProducts(categoryId);
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 상품 등록 (관리자 전용)
    @PostMapping("/admin/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // 상품 수정 (관리자 전용)
    @PutMapping("/admin/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> updatedProduct = productService.updateProduct(id, product);
        return updatedProduct.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 상품 삭제 (soft delete, 관리자 전용)
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> deletedProduct = productService.deleteProduct(id);
        if (deletedProduct.isPresent()) {
            return ResponseEntity.ok("Product deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
