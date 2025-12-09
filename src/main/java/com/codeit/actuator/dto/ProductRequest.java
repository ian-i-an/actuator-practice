package com.codeit.actuator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 등록/수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "상품명은 필수입니다")
    @Size(min = 2, max = 100, message = "상품명은 2-100자여야 합니다")
    private String name;
    
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
    private String description;
    
    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
    private Integer price;
    
    @NotNull(message = "재고는 필수입니다")
    @Min(value = 0, message = "재고는 0개 이상이어야 합니다")
    private Integer stock;
    
    @NotBlank(message = "카테고리는 필수입니다")
    private String category;
}

