package com.codeit.actuator.dto;

import com.codeit.actuator.domain.Order;
import com.codeit.actuator.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 응답 DTO
 */
@Getter
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private String orderNumber;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer totalAmount;
    private OrderStatus status;
    private String customerName;
    private String customerEmail;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getDeliveryAddress(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}

