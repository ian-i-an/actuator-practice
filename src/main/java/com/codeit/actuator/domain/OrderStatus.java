package com.codeit.actuator.domain;

/**
 * 주문 상태
 */
public enum OrderStatus {
    PENDING,      // 대기
    CONFIRMED,    // 확인
    SHIPPED,      // 배송 중
    DELIVERED,    // 배송 완료
    CANCELLED     // 취소
}

