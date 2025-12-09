package com.codeit.actuator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    
    @NotNull(message = "상품 ID는 필수입니다")
    @Positive(message = "상품 ID는 양수여야 합니다")
    private Long productId;
    
    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "최소 1개 이상 주문해야 합니다")
    @Max(value = 100, message = "한 번에 최대 100개까지 주문 가능합니다")
    private Integer quantity;
    
    @NotBlank(message = "고객명은 필수입니다")
    @Size(max = 100, message = "고객명은 100자를 초과할 수 없습니다")
    private String customerName;
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String customerEmail;
    
    @NotBlank(message = "배송 주소는 필수입니다")
    @Size(max = 200, message = "배송 주소는 200자를 초과할 수 없습니다")
    private String deliveryAddress;
}

