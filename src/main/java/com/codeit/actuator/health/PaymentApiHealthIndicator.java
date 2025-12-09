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
//    private final
    private final PaymentService paymentService;
    @Override
    public Health health() {

        //결제 API 상태 확인
        boolean isAvailable = paymentService.checkHealth();

        if(isAvailable){
            return Health.up().build();
        }

        return null;
    }
}
