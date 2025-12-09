package com.codeit.actuator.controller;

import com.codeit.actuator.dto.OrderRequest;
import com.codeit.actuator.dto.OrderResponse;
import com.codeit.actuator.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 컨트롤러
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 전체 주문 조회
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("전체 주문 조회 요청");
        
        List<OrderResponse> orders = orderService.findAll();
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * 주문 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        log.info("주문 조회 요청 - ID: {}", id);
        
        OrderResponse order = orderService.findById(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 주문번호로 조회
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(
            @PathVariable String orderNumber) {
        log.info("주문 조회 요청 - 주문번호: {}", orderNumber);
        
        OrderResponse order = orderService.findByOrderNumber(orderNumber);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 이메일로 주문 조회
     */
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderResponse>> getOrdersByEmail(
            @PathVariable String email) {
        log.info("이메일로 주문 조회 요청 - 이메일: {}", email);
        
        List<OrderResponse> orders = orderService.findByCustomerEmail(email);
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {
        log.info("주문 생성 요청 - 상품ID: {}, 고객: {}", 
                request.getProductId(), request.getCustomerName());
        
        OrderResponse order = orderService.create(request);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 주문 확인
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id) {
        log.info("주문 확인 요청 - ID: {}", id);
        
        OrderResponse order = orderService.confirm(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 배송 시작
     */
    @PostMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable Long id) {
        log.info("배송 시작 요청 - ID: {}", id);
        
        OrderResponse order = orderService.ship(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 배송 완료
     */
    @PostMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long id) {
        log.info("배송 완료 요청 - ID: {}", id);
        
        OrderResponse order = orderService.deliver(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * 주문 취소
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        log.info("주문 취소 요청 - ID: {}", id);
        
        OrderResponse order = orderService.cancel(id);
        
        return ResponseEntity.ok(order);
    }
}

