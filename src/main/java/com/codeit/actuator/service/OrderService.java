package com.codeit.actuator.service;

import com.codeit.actuator.domain.Order;
import com.codeit.actuator.domain.Product;
import com.codeit.actuator.dto.OrderRequest;
import com.codeit.actuator.dto.OrderResponse;
import com.codeit.actuator.exception.OrderNotFoundException;
import com.codeit.actuator.exception.ProductNotFoundException;
import com.codeit.actuator.repository.OrderRepository;
import com.codeit.actuator.repository.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 서비스
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    //메트릭 추가!
    private final Counter orderCreatedCounter;
    private final Counter orderCancelledCounter;

    private final DistributionSummary orderAmountSummary;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        PaymentService paymentService,
                        MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;

        // 메트릭 등록
        this.orderCreatedCounter = Counter.builder("orders.created") // 메트릭 이름
                .description("Total number of orders created") // 설명 (선택)
                .tag("type", "order") // 태그 (필터링용)
                .register(meterRegistry); // 레지스트리에 메트릭 등록!
        // 주문 취소 카운터
        this.orderCancelledCounter = Counter.builder("orders.cancelled")
                .description("Total number of orders cancelled")
                .tag("type", "orders")
                .register(meterRegistry);

        this.orderAmountSummary = DistributionSummary.builder("orders.amount")
                .description("Distribution of order amount")
                .baseUnit("KRW")
                .register(meterRegistry);
    }

    /**
     * 전체 주문 조회
     */
    public List<OrderResponse> findAll() {
        log.debug("전체 주문 조회");

        List<Order> orders = orderRepository.findAll();

        log.debug("조회된 주문 개수: {}", orders.size());

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 ID로 조회
     */
    public OrderResponse findById(Long id) {
        log.debug("주문 조회 - ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        log.debug("주문 조회 완료 - ID: {}, 주문번호: {}", order.getId(), order.getOrderNumber());

        return OrderResponse.from(order);
    }

    /**
     * 주문번호로 조회
     */
    public OrderResponse findByOrderNumber(String orderNumber) {
        log.debug("주문 조회 - 주문번호: {}", orderNumber);

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        return OrderResponse.from(order);
    }

    /**
     * 이메일로 주문 조회
     */
    public List<OrderResponse> findByCustomerEmail(String email) {
        log.debug("이메일로 주문 조회 - 이메일: {}", email);

        List<Order> orders = orderRepository.findByCustomerEmail(email);

        log.debug("조회된 주문 개수: {}", orders.size());

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 생성
     */
    @Transactional
    public OrderResponse create(OrderRequest request) {
        log.info("주문 생성 시작 - 상품ID: {}, 수량: {}, 고객: {}",
                request.getProductId(), request.getQuantity(), request.getCustomerName());

        // 상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        log.debug("상품 조회 완료 - 이름: {}, 재고: {}개", product.getName(), product.getStock());

        // 재고 확인 및 차감
        product.decreaseStock(request.getQuantity());
        log.debug("재고 차감 완료 - 남은 재고: {}개", product.getStock());

        // 주문 생성
        Order order = Order.create(
                product,
                request.getQuantity(),
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getDeliveryAddress()
        );

        Order saved = orderRepository.save(order);

        // 결제 처리 (외부 API 호출 시뮬레이션)
        boolean paymentSuccess = paymentService.processPayment(
                saved.getOrderNumber(),
                saved.getTotalAmount()
        );

        if (!paymentSuccess) {
            // 결제 실패 시 재고 복구
            product.increaseStock(request.getQuantity());
            throw new IllegalStateException("결제 처리에 실패했습니다");
        }

        //메트릭 증가!
        orderCreatedCounter.increment();
        orderAmountSummary.record(order.getTotalAmount()); // 금액 기록!

        log.info("주문 생성 완료 - 주문ID: {}, 주문번호: {}, 금액: {}원",
                saved.getId(), saved.getOrderNumber(), saved.getTotalAmount());

        return OrderResponse.from(saved);
    }

    /**
     * 주문 확인
     */
    @Transactional
    public OrderResponse confirm(Long id) {
        log.info("주문 확인 - ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.confirm();

        log.info("주문 확인 완료 - 주문번호: {}", order.getOrderNumber());

        return OrderResponse.from(order);
    }

    /**
     * 주문 배송 시작
     */
    @Transactional
    public OrderResponse ship(Long id) {
        log.info("배송 시작 - ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.ship();

        log.info("배송 시작 완료 - 주문번호: {}", order.getOrderNumber());

        return OrderResponse.from(order);
    }

    /**
     * 주문 배송 완료
     */
    @Transactional
    public OrderResponse deliver(Long id) {
        log.info("배송 완료 - ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.deliver();

        log.info("배송 완료 처리 - 주문번호: {}", order.getOrderNumber());

        return OrderResponse.from(order);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public OrderResponse cancel(Long id) {
        log.info("주문 취소 시작 - ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!order.isCancellable()) {
            throw new IllegalStateException("취소할 수 없는 주문입니다");
        }

        // 주문 취소
        order.cancel();

        // 재고 복구
        order.getProduct().increaseStock(order.getQuantity());

        // 환불 처리 (외부 API 호출 시뮬레이션)
        boolean refundSuccess = paymentService.refund(
                order.getOrderNumber(),
                order.getTotalAmount()
        );

        if (!refundSuccess) {
            log.warn("환불 처리 실패 - 주문번호: {}", order.getOrderNumber());
        }

        // 메트릭 증가!
        orderCancelledCounter.increment();

        log.info("주문 취소 완료 - 주문번호: {}, 환불금액: {}원",
                order.getOrderNumber(), order.getTotalAmount());

        return OrderResponse.from(order);
    }
}

