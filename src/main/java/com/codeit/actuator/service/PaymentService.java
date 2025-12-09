package com.codeit.actuator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 결제 서비스 (외부 API 호출 시뮬레이션)
 * 
 * 실제로는 외부 결제 API를 호출하지만, 
 * 이 예제에서는 시뮬레이션만 수행합니다.
 * 
 * Actuator 실습 시 이 서비스의 Health Check를 구현할 예정입니다.
 */
@Service
@Slf4j
public class PaymentService {
    
    private boolean isAvailable = true;  // 외부 API 가용성 시뮬레이션
    
    /**
     * 결제 처리
     * 
     * @param orderNumber 주문번호
     * @param amount 결제 금액
     * @return 결제 성공 여부
     */
    public boolean processPayment(String orderNumber, Integer amount) {
        log.info("결제 처리 시작 - 주문번호: {}, 금액: {}원", orderNumber, amount);
        
        try {
            // 외부 API 호출 시뮬레이션 (실제로는 HTTP 요청)
            simulateExternalApiCall();
            
            if (!isAvailable) {
                log.warn("결제 API 사용 불가");
                return false;
            }
            
            // 결제 처리 로직 (시뮬레이션)
            Thread.sleep(100);  // API 호출 지연 시뮬레이션
            
            log.info("결제 처리 완료 - 주문번호: {}", orderNumber);
            return true;
            
        } catch (Exception e) {
            log.error("결제 처리 실패 - 주문번호: {}", orderNumber, e);
            return false;
        }
    }
    
    /**
     * 환불 처리
     * 
     * @param orderNumber 주문번호
     * @param amount 환불 금액
     * @return 환불 성공 여부
     */
    public boolean refund(String orderNumber, Integer amount) {
        log.info("환불 처리 시작 - 주문번호: {}, 금액: {}원", orderNumber, amount);
        
        try {
            // 외부 API 호출 시뮬레이션
            simulateExternalApiCall();
            
            if (!isAvailable) {
                log.warn("결제 API 사용 불가");
                return false;
            }
            
            // 환불 처리 로직 (시뮬레이션)
            Thread.sleep(100);
            
            log.info("환불 처리 완료 - 주문번호: {}", orderNumber);
            return true;
            
        } catch (Exception e) {
            log.error("환불 처리 실패 - 주문번호: {}", orderNumber, e);
            return false;
        }
    }
    
    /**
     * 결제 API 상태 확인
     * 
     * @return API 가용 여부
     */
    public boolean checkHealth() {
        log.debug("결제 API 상태 확인");
        
        try {
            simulateExternalApiCall();
            return isAvailable;
        } catch (Exception e) {
            log.error("결제 API 상태 확인 실패", e);
            return false;
        }
    }
    
    /**
     * 외부 API 호출 시뮬레이션
     */
    private void simulateExternalApiCall() {
        // 실제로는 HTTP 클라이언트로 외부 API 호출
        // 예: RestTemplate, WebClient, Feign 등
        
        // 여기서는 단순히 가용성 상태만 체크
        if (Math.random() < 0.05) {  // 5% 확률로 일시적 장애 시뮬레이션
            isAvailable = false;
            log.warn("결제 API 일시적 장애 발생");
            
            // 1초 후 자동 복구
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    isAvailable = true;
                    log.info("결제 API 복구됨");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
    
    /**
     * API 가용성 강제 설정 (테스트용)
     */
    public void setAvailability(boolean available) {
        this.isAvailable = available;
        log.info("결제 API 가용성 변경: {}", available);
    }
}

