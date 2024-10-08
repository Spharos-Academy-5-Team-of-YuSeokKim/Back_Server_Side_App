package com.hummingbird.kr.starbuckslike.member.application;

import com.hummingbird.kr.starbuckslike.member.dto.in.*;
import com.hummingbird.kr.starbuckslike.member.dto.out.*;

public interface MemberService {

    /**
     * 회원 가입 후 경과 기간 확인
     * @param memberUID
     * @return DTO that has memberUID and DateDiff
     */
    PassageResponseDTO passage(String memberUID);

    /**
     * 회원 정보 수정
     * @param requestDTO
     */
    void update(MemberUpdateRequestDTO requestDTO);

    /**
     * 회원 정보 조회
     * @param memberUID
     * @return DTO that has memberUID and MemberInfo
     */
    MemberInfoResponseDTO info(String memberUID);


}
