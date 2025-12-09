package com.codeit.actuator.service;

import com.codeit.actuator.domain.Product;
import com.codeit.actuator.dto.ProductRequest;
import com.codeit.actuator.dto.ProductResponse;
import com.codeit.actuator.exception.ProductNotFoundException;
import com.codeit.actuator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 서비스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    
    /**
     * 전체 상품 조회
     */
    public List<ProductResponse> findAll() {
        log.debug("전체 상품 조회");
        
        List<Product> products = productRepository.findAll();
        
        log.debug("조회된 상품 개수: {}", products.size());
        
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 상품 ID로 조회
     */
    public ProductResponse findById(Long id) {
        log.debug("상품 조회 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        log.debug("상품 조회 완료 - ID: {}, 이름: {}", product.getId(), product.getName());
        
        return ProductResponse.from(product);
    }
    
    /**
     * 카테고리별 상품 조회
     */
    public List<ProductResponse> findByCategory(String category) {
        log.debug("카테고리별 상품 조회 - 카테고리: {}", category);
        
        List<Product> products = productRepository.findByCategory(category);
        
        log.debug("조회된 상품 개수: {}", products.size());
        
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 상품명 검색
     */
    public List<ProductResponse> searchByName(String keyword) {
        log.debug("상품명 검색 - 키워드: {}", keyword);
        
        List<Product> products = productRepository.findByNameContaining(keyword);
        
        log.debug("검색된 상품 개수: {}", products.size());
        
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 상품 등록
     */
    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.info("상품 등록 시작 - 이름: {}, 가격: {}원", request.getName(), request.getPrice());
        
        Product product = Product.create(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory()
        );
        
        Product saved = productRepository.save(product);
        
        log.info("상품 등록 완료 - ID: {}, 이름: {}", saved.getId(), saved.getName());
        
        return ProductResponse.from(saved);
    }
    
    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        log.info("상품 수정 시작 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        product.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory()
        );
        
        log.info("상품 수정 완료 - ID: {}, 이름: {}", product.getId(), product.getName());
        
        return ProductResponse.from(product);
    }
    
    /**
     * 상품 삭제
     */
    @Transactional
    public void delete(Long id) {
        log.info("상품 삭제 시작 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        productRepository.delete(product);
        
        log.info("상품 삭제 완료 - ID: {}", id);
    }
}

