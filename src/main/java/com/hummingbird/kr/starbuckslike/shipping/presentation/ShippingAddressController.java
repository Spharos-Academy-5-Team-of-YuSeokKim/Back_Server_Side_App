package com.hummingbird.kr.starbuckslike.shipping.presentation;

import com.hummingbird.kr.starbuckslike.auth.domain.AuthUserDetail;
import com.hummingbird.kr.starbuckslike.common.entity.BaseResponse;
import com.hummingbird.kr.starbuckslike.common.entity.BaseResponseStatus;
import com.hummingbird.kr.starbuckslike.shipping.application.*;
import com.hummingbird.kr.starbuckslike.shipping.vo.in.*;
import com.hummingbird.kr.starbuckslike.shipping.vo.out.*;
import com.hummingbird.kr.starbuckslike.shipping.dto.in.*;
import com.hummingbird.kr.starbuckslike.shipping.dto.out.*;
import com.hummingbird.kr.starbuckslike.shipping.dto.ShippingAddressDTO;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/shipping")
public class ShippingAddressController {

    private final ShippingService shippingService;

    @Autowired
    public ShippingAddressController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping("/add")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "배송지 추가",
            description = "배송지를 추가합니다.", tags = {"Shipping"})
    public BaseResponse<Void> add(@RequestBody ShippingAddressAddRequestVO requestVO, @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        ShippingAddressAddRequestDTO requestDTO = requestVO.toDTO(authUserDetail.getUsername());
        shippingService.add(requestDTO);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/update")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "배송지 수정",
            description = "배송지를 수정합니다.", tags = {"Shipping"})
    public BaseResponse<Void> update(@RequestBody ShippingAddressUpdateRequestVO requestVO, @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        ShippingAddressUpdateRequestDTO requestDTO = requestVO.toDTO(authUserDetail.getUsername());
        shippingService.update(requestDTO);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/delete")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "배송지 삭제",
            description = "배송지를 삭제합니다.", tags = {"Shipping"})
    public BaseResponse<Void> delete(@RequestBody ShippingAddressDeleteRequestVO requestVO, @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        ShippingAddressDeleteRequestDTO requestDTO = requestVO.toDTO(authUserDetail.getUsername());
        shippingService.delete(requestDTO);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/list")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "배송지 목록",
            description = "배송지 목록을 조회합니다.", tags = {"Shipping"})
    public BaseResponse<List<ShippingAddressDTO>> shippingList(@AuthenticationPrincipal AuthUserDetail authUserDetail) {
        List<ShippingAddressDTO> responseDTO = shippingService.shippingList(authUserDetail.getUsername());
        return new BaseResponse<>(responseDTO);
    }

    @PostMapping("/set-default")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "기본 배송지 설정",
            description = "기본 배송지를 설정합니다.", tags = {"Shipping"})
    public BaseResponse<Void> setDefault(@RequestBody ShippingAddressSetDefaultRequestVO requestVO,
            @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        shippingService.setDefault(requestVO.toDTO(authUserDetail.getUsername()));
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/get-detail")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
            summary = "배송지 조회",
            description = "배송지 정보를 조회합니다.", tags = {"Shipping"})
    public BaseResponse<ShippingAddressGetDetailResponseVO> getDetail(@RequestBody ShippingAddressGetDetailRequestVO requestVO,
             @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        ShippingAddressGetDetailResponseDTO responseDTO = shippingService
                .getDetail(ShippingAddressGetDetailRequestDTO.from(requestVO, authUserDetail.getUsername()));
        return new BaseResponse<>(responseDTO.toVO());
    }

    @PostMapping("/default-id")
    @Operation(security = @SecurityRequirement(name = "Bearer Auth"),
               summary = "기본 배송지 ID 확인",
            description = "기본 배송지의 id를 가져옵니다", tags = {"Shipping"})
    public BaseResponse<ShippingDefaultIDResponseVO> defaultID(@AuthenticationPrincipal AuthUserDetail authUserDetail) {
        return new BaseResponse<>(shippingService.getDefaultID(authUserDetail.getUsername()).toVO());
    }

    
}
