package com.codeit.actuator.exception;

/**
 * 상품을 찾을 수 없을 때 발생하는 예외
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(Long productId) {
        super(String.format("상품을 찾을 수 없습니다. ID: %d", productId));
    }
}

