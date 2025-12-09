package com.codeit.actuator.config;

import com.codeit.actuator.domain.Product;
import com.codeit.actuator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 초기 데이터 생성
 * 애플리케이션 시작 시 샘플 상품 데이터를 자동으로 생성합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) {
        log.info("초기 데이터 생성 시작");
        
        // 샘플 상품 데이터 생성
        createSampleProducts();
        
        log.info("초기 데이터 생성 완료");
    }
    
    private void createSampleProducts() {
        // 전자제품
        productRepository.save(Product.create(
                "노트북",
                "고성능 게이밍 노트북",
                1500000,
                10,
                "전자제품"
        ));
        
        productRepository.save(Product.create(
                "스마트폰",
                "최신 스마트폰",
                1000000,
                20,
                "전자제품"
        ));
        
        productRepository.save(Product.create(
                "태블릿",
                "10인치 태블릿",
                500000,
                15,
                "전자제품"
        ));
        
        // 의류
        productRepository.save(Product.create(
                "티셔츠",
                "면 100% 티셔츠",
                25000,
                50,
                "의류"
        ));
        
        productRepository.save(Product.create(
                "청바지",
                "스키니 청바지",
                80000,
                30,
                "의류"
        ));
        
        // 도서
        productRepository.save(Product.create(
                "Spring Boot 완벽 가이드",
                "Spring Boot 입문부터 실전까지",
                35000,
                100,
                "도서"
        ));
        
        productRepository.save(Product.create(
                "Clean Code",
                "클린 코드 - 애자일 소프트웨어 장인 정신",
                30000,
                80,
                "도서"
        ));
        
        // 식품
        productRepository.save(Product.create(
                "유기농 쌀",
                "국내산 유기농 쌀 10kg",
                45000,
                200,
                "식품"
        ));
        
        productRepository.save(Product.create(
                "프리미엄 커피",
                "원두 커피 1kg",
                25000,
                150,
                "식품"
        ));
        
        productRepository.save(Product.create(
                "올리브유",
                "엑스트라 버진 올리브유",
                18000,
                100,
                "식품"
        ));
        
        log.info("샘플 상품 10개 생성 완료");
    }
}

