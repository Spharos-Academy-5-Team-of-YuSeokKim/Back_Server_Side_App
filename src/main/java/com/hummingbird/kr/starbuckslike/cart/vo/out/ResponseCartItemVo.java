package com.hummingbird.kr.starbuckslike.cart.vo.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCartItemVo {
    // 장바구니 id
    private Long cartId;
    // 선택 상태 여부
    private Boolean isChecked;
    // 입력 데이터 (Cart 필드) , 입력데이터 없는 옵션상품이면 null 들어감. 추후 개선 필요
    private String inputData;
    // 상품 id (구매를 위해)
    private Long productId;
    // 상품명
    private String productName;
    // 상품 옵션 id (구매를 위해)
    private Long optionId;
    // 상품 옵션명
    private String optionName;
    // 장바구니 선택 수량
    private Integer cartQuantity;
    // 옵션 가격
    private Integer price;
    // 옵션 할인율
    private Float discountRate;
}
