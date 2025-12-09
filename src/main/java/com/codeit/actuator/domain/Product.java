package com.codeit.actuator.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 상품 엔티티
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Integer price;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false, length = 50)
    private String category;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 정적 팩토리 메서드
    public static Product create(String name, String description, Integer price, Integer stock, String category) {
        Product product = new Product();
        product.name = name;
        product.description = description;
        product.price = price;
        product.stock = stock;
        product.category = category;
        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();
        return product;
    }
    
    // 비즈니스 메서드
    public void update(String name, String description, Integer price, Integer stock, String category) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (price != null) this.price = price;
        if (stock != null) this.stock = stock;
        if (category != null) this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException(
                String.format("재고가 부족합니다. 현재 재고: %d, 요청 수량: %d", this.stock, quantity)
            );
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void increaseStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}

