package com.hummingbird.kr.starbuckslike.product.infrastructure.search;

import com.hummingbird.kr.starbuckslike.common.utils.CursorPage;
import com.hummingbird.kr.starbuckslike.product.dto.out.*;
import com.hummingbird.kr.starbuckslike.product.infrastructure.condition.ProductCondition;
import com.hummingbird.kr.starbuckslike.product.vo.out.ProductCartQtyResponseVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductSearch {


    /**
     * 기획전에 해당하는 상품 리스트 조회
     */
    // 개선 - 기획전에 해당하는 상품 리스트 조회
    List<Long> findProductIdListByExhibitionId(Long exhibitionId);
    ProductListResponseDto findProductListDtoByProductId(Long productId);


    /**
     * 상품 리스트 조회
     * 필터링 조건[카테고리,가격,기획전] , 정렬 조건(최신순, 높은 가격, 낮은 가격)
     * offset based
     */
     // api 단건으로 분리
     Slice<Long> searchProductIdsV1(ProductCondition productCondition, Pageable pageable);

     // 상품아이디로 상품 리스트 이미지 단건 조회
     ProductListImageResponseDto findProductListImageResponseDtoById(Long productId);
     // 상품아이디로 상품 리스트 정보 단건 조회
     ProductListInfoResponseDto findProductListInfoResponseDtoById(Long productId);

    /**
     * 위시리스트 상품 조회
     * offset based
     */
    // 위리리스트 상품 id 조회. id로 findProductListImageResponseDtoById, findProductListInfoResponseDtoById
    Slice<Long> searchWishProductIdsV1(Pageable pageable, String memberUid);

    /**
     *  상품 디테일 정보 (상품상세 , 상품의 이미지)
     */
    //상품 디테일 상품 이미지+상품명 가격 등 조회
    ProductInfoResponseDto findProductInfoByIdV2(Long productId, String memberUid); // 개선 :  장바구니 개수, 좋아요 개수 포함

    // 상품 id로 상품 상세정보(에티터 html)  조회
    ProductDetailResponseDto findProductDetailDtoById(Long productId);

    // 상품 Id로 상품 이미지 조회
    List<ProductImageResponseDto> findProductImageDtoById(Long productId);

    // 상품의 옵션 조회
    List<ProductOptionResponseDto> findProductOptionDtoById(Long productId);

    // 상품 좋아요 여부 조회
    ProductIsWishedResponseDto findProductIsWishedResponseDtoById(Long productId, String memberUid);
    // 장바구니에 상품을 담은 수량
    ProductCartQtyResponseDto findProductCartQtyResponseDto(Long productId, String memberUid);

    /**
     * 베스트 상품, 관심 상품
     * 베스트 상품 조회 : 리뷰 통계 테이블 TOP 30
     * 관심 상품 조회 : wish 통계 테이블 TOP 10
     */
    // 관심 상품 조회
    List<Long> searchMostWishedProductIds();

    // 카테고리 별  베스트 상품 조회
    List<Long> searchBestProductIds(String topCategoryCode);
}
