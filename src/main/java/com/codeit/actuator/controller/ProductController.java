package com.codeit.actuator.controller;

import com.codeit.actuator.dto.ProductRequest;
import com.codeit.actuator.dto.ProductResponse;
import com.codeit.actuator.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 상품 컨트롤러
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 전체 상품 조회
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("전체 상품 조회 요청");
        
        List<ProductResponse> products = productService.findAll();
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * 상품 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("상품 조회 요청 - ID: {}", id);
        
        ProductResponse product = productService.findById(id);
        
        return ResponseEntity.ok(product);
    }
    
    /**
     * 카테고리별 상품 조회
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String category) {
        log.info("카테고리별 상품 조회 요청 - 카테고리: {}", category);
        
        List<ProductResponse> products = productService.findByCategory(category);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * 상품명 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {
        log.info("상품 검색 요청 - 키워드: {}", keyword);
        
        List<ProductResponse> products = productService.searchByName(keyword);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("상품 등록 요청 - 이름: {}", request.getName());
        
        ProductResponse product = productService.create(request);
        
        return ResponseEntity.ok(product);
    }
    
    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("상품 수정 요청 - ID: {}", id);
        
        ProductResponse product = productService.update(id, request);
        
        return ResponseEntity.ok(product);
    }
    
    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("상품 삭제 요청 - ID: {}", id);
        
        productService.delete(id);
        
        return ResponseEntity.noContent().build();
    }
}

