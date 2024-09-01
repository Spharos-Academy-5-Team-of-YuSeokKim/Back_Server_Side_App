package com.hummingbird.kr.starbuckslike.temp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 기획전 상세정보 DTO
 * @author 허정현
 */
@Data
@Builder
@NoArgsConstructor
public class ExhibitionDetailDto {
    private String detail; // 긴 에티터 데이터 (html)

    @QueryProjection
    public ExhibitionDetailDto(String detail) {
        this.detail = detail;
    }
}
