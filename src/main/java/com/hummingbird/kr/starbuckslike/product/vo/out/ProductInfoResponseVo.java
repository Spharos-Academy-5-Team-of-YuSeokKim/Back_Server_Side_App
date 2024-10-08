package com.hummingbird.kr.starbuckslike.product.vo.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoResponseVo {
    private String name; // 상품명
    private Integer price; // 상품 가격
    private Boolean isNew; // 신규 상품 여부
    private String shortDescription; // 짧은 상품설명 (텍스트)
    private Boolean isDiscounted; // 할인 여부
    private Float discountRate; // 할인율

    private Long wishCount; // 해당 상품의 좋아요 개수
}
