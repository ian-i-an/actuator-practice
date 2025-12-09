package com.codeit.actuator.exception;

/**
 * 주문을 찾을 수 없을 때 발생하는 예외
 */
public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(Long orderId) {
        super(String.format("주문을 찾을 수 없습니다. ID: %d", orderId));
    }
    
    public OrderNotFoundException(String orderNumber) {
        super(String.format("주문을 찾을 수 없습니다. 주문번호: %s", orderNumber));
    }
}

