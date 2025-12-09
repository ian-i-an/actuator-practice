package com.codeit.actuator.health;

import com.codeit.actuator.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApiHealthIndicator implements HealthIndicator {

    /*
     * 외부 api와 통신을 진핼하라 때는 RestTemplate 등의 객체를 이용해서 진행하겠지만,
     * 우리는 없기 때문에 PaymentService와 소통하겠음
     * */
//    private final RestTemplate restTemplate
    private final PaymentService paymentService;

    @Override
    public Health health() {

        try {
            //결제 API 상태 확인
            boolean isAvailable = paymentService.checkHealth();

            if (isAvailable) {
                return Health.up()
                        .withDetail("api", "Payment API")
                        .withDetail("status", "Available")
                        .withDetail("message", "결제 API가 정상 동작중입니다.")
                        .build();
            } else {
                return Health.up()
                        .withDetail("api", "Payment API")
                        .withDetail("status", "Unavailable")
                        .withDetail("message", "결제 API를 사용할 수 없습니다.")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("api", "Payment API")
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "결제 API 상태 확인 불가")
                    .build();
        }
    }
}
