package com.codeit.actuator.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 엔티티
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;
    
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @Column(name = "customer_email", nullable = false, length = 100)
    private String customerEmail;
    
    @Column(name = "delivery_address", nullable = false, length = 200)
    private String deliveryAddress;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 정적 팩토리 메서드
    public static Order create(
            Product product,
            Integer quantity,
            String customerName,
            String customerEmail,
            String deliveryAddress
    ) {
        Order order = new Order();
        order.orderNumber = generateOrderNumber();
        order.product = product;
        order.quantity = quantity;
        order.totalAmount = product.getPrice() * quantity;
        order.status = OrderStatus.PENDING;
        order.customerName = customerName;
        order.customerEmail = customerEmail;
        order.deliveryAddress = deliveryAddress;
        order.createdAt = LocalDateTime.now();
        order.updatedAt = LocalDateTime.now();
        return order;
    }
    
    // 주문번호 생성 (ORD-20251208-001 형식)
    private static String generateOrderNumber() {
        return String.format("ORD-%s-%03d",
                LocalDateTime.now().toString().substring(0, 10).replace("-", ""),
                (int) (Math.random() * 1000));
    }
    
    // 비즈니스 메서드
    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("대기 중인 주문만 확인할 수 있습니다.");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void ship() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("확인된 주문만 배송할 수 있습니다.");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("배송 중인 주문만 배송 완료 처리할 수 있습니다.");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("배송 완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isCancellable() {
        return this.status != OrderStatus.DELIVERED && this.status != OrderStatus.CANCELLED;
    }
}

