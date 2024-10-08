package com.hummingbird.kr.starbuckslike.auth.vo.in;

import com.hummingbird.kr.starbuckslike.auth.dto.in.OauthRegisterRequestDTO;

import lombok.Getter;

@Getter
public class OauthRegisterRequestVO {
    private String oauthID;
    private String oauthType;
    
    public OauthRegisterRequestDTO toDTO(String memberUID) {
        return OauthRegisterRequestDTO.builder()
                .memberUID(memberUID)
                .oauthID(this.oauthID)
                .oauthType(this.oauthType)
                .build();
    }
}
