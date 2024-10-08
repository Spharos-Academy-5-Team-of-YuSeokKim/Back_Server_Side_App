package com.hummingbird.kr.starbuckslike.auth.presentation;

import com.hummingbird.kr.starbuckslike.auth.application.AuthService;
import com.hummingbird.kr.starbuckslike.auth.domain.AuthUserDetail;
import com.hummingbird.kr.starbuckslike.auth.dto.in.UpdatePWRequestDTO;
import com.hummingbird.kr.starbuckslike.auth.dto.out.*;
import com.hummingbird.kr.starbuckslike.auth.vo.in.*;
import com.hummingbird.kr.starbuckslike.auth.vo.out.*;
import com.hummingbird.kr.starbuckslike.common.entity.BaseResponse;
import com.hummingbird.kr.starbuckslike.common.entity.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@ComponentScan(basePackages = {"com.hummingbird.kr.starbuckslike.auth"})
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController( AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Member Login API", description = "Member Login API", tags = {"Auth"})
    @PostMapping("/login")
    public BaseResponse<LoginResponseVO> login(@RequestBody LoginRequestVO loginRequestVO) {
        log.info("login loginID: {}", loginRequestVO.getLoginID());
        try {
            LoginResponseDTO loginResponseDTO = authService.login((loginRequestVO.toDTO()));
            return new BaseResponse<LoginResponseVO>(loginResponseDTO.toVO());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.FAILED_TO_LOGIN);}
    }
    @Operation(summary = "Member Register API", description = "Member Register API", tags = {"Auth"})
    @PostMapping("/register")
    public BaseResponse<Void> register(@RequestBody RegisterRequestVO registerRequestVO) {
        authService.register(registerRequestVO.toDTO());
        return new BaseResponse<>();
    }
    @Operation(summary = "PW re_init API", description = "PW reset API, email 인증 필요 합니다", tags = {"Auth"})
    @PostMapping("/resetPW")
    public BaseResponse<Void> resetPW(@RequestBody ResetPWRequestVO resetPWRequestVO) {
        authService.resetPW(resetPWRequestVO.toDTO());
        return new BaseResponse<>();
    }
    @Operation(security = @SecurityRequirement(name = "Bearer Auth")
            ,summary = "PW update API", description = "PW update API", tags = {"Auth"})
    @PostMapping("/updatePW")
    public BaseResponse<Void> updatePW(@RequestBody UpdatePWRequestVO updatePWRequestVO, @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        authService.updatePW(new UpdatePWRequestDTO(updatePWRequestVO.getNewPassword(), updatePWRequestVO.getOldPassword(), authUserDetail.getUsername()));
        return new BaseResponse<>();
    }
    @Operation(security = @SecurityRequirement(name = "Bearer Auth")
            , summary = "Member Logout API", description = "Member Logout API", tags = {"Auth"})
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        LogoutRequestVO logoutRequestVO = new LogoutRequestVO();
        logoutRequestVO.setAccessToken(token);
        logoutRequestVO.setRefreshToken(token);
        authService.logout(logoutRequestVO.toDTO());
        return new BaseResponse<>();
    }
    @Operation(summary = "Member Find ID API", description = "Member Find ID API, 이메일 인증 필요합니다", tags = {"Auth"})
    @PostMapping("/findID")
    public BaseResponse<FindIDResponseVO> findID(@RequestBody FindIDRequestVO findIDRequestVO) {
        FindIDResponseDTO responseDTO = authService.findID(findIDRequestVO.toDTO());
        return new BaseResponse<FindIDResponseVO>(responseDTO.toVO());
    }

    @Operation(summary = "Find Duplication of Email API", description = "Find Duplication of Email API", tags = {"Auth"})
    @PostMapping("checkEmail")
    public BaseResponse<CheckEmailResponseVO> checkEmail(@RequestBody CheckEmailRequestVO checkEmailRequestVO) {
        CheckEmailResponseDTO checkEmailResponseDTO = authService.checkEmail(checkEmailRequestVO.toDTO());
        return new BaseResponse<CheckEmailResponseVO>(checkEmailResponseDTO.toVO());
    }

    @Operation(summary = "Find Duplication of LoginID API", description = "Find Duplication of LoginID API", tags = {"Auth"})
    @PostMapping("checkLoginID")
    public BaseResponse<CheckLoginIDResponseVO> checkLoginID(@RequestBody CheckLoginIDRequestVO checkLoginIDRequestVO) {
        CheckLoginIDResponseDTO checkLoginIDResponseDTO = authService.checkLoginID(checkLoginIDRequestVO.toDTO());
        return new BaseResponse<CheckLoginIDResponseVO>(checkLoginIDResponseDTO.toVO());
    }

    @Operation(summary = "Find Duplication of Phone API", description = "Find Duplication of Phone API", tags = {"Auth"})
    @PostMapping("checkPhone")
    public BaseResponse<CheckPhoneResponseVO> checkPhone(@RequestBody CheckPhoneRequestVO checkPhoneRequestVO) {
        CheckPhoneResponseDTO checkPhoneResponseDTO = authService.checkPhone(checkPhoneRequestVO.toDTO());
        return new BaseResponse<>(checkPhoneResponseDTO.toVO());
    }

    @Operation(summary = "Send Email Verification Code API", description = "Send Email Verification Code API", tags = {"Auth"})
    @PostMapping("email/request")
    public BaseResponse<Void> sendEmailVerificationCode(@RequestBody EmailVerificationRequestVO emailVerificationRequestVO) {
        authService.sendEmailVerificationCode(emailVerificationRequestVO.toDTO());
        return new BaseResponse<>();
    }

    @Operation(summary = "Check Email Verification Code API", description = "Check Email Verification Code API", tags = {"Auth"})
    @PostMapping("email/check")
    public BaseResponse<Void> checkEmailVerificationCode(@RequestBody EmailVerificationCheckRequestVO emailVerificationCheckRequestVO) {
        authService.checkEmailVerificationCode(emailVerificationCheckRequestVO.toDTO());
        return new BaseResponse<>();
    }

    @Operation(summary = "Send Phone Verification Code API", description = "Send Phone Verification Code API", tags = {"Auth"})
    @PostMapping("phone/request")
    public BaseResponse<Void> sendPhoneVerificationCode(@RequestBody PhoneVerificationRequestVO phoneVerificationRequestVO) {
        authService.sendPhoneVerificationCode(phoneVerificationRequestVO.toDTO());
        return new BaseResponse<>();
    }

    @Operation(summary = "Check Phone Verification Code API", description = "Check Phone Verification Code API", tags = {"Auth"})
    @PostMapping("phone/check")
    public BaseResponse<Void> checkPhoneVerificationCode(@RequestBody PhoneVerificationCheckRequestVO phoneVerificationCheckRequestVO) {
        authService.checkPhoneVerificationCode(phoneVerificationCheckRequestVO.toDTO());
        return new BaseResponse<>();
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Auth")
    ,summary = "OAuth Register API", description = "OAuth Register API", tags = {"Auth"})
    @PostMapping("oauth/register")
    public BaseResponse<Void> oauthRegister(@RequestBody OauthRegisterRequestVO oauthRegisterRequestVO, @AuthenticationPrincipal AuthUserDetail authUserDetail) {
        authService.oauthRegister(oauthRegisterRequestVO.toDTO(authUserDetail.getUsername()));
        return new BaseResponse<>();
    }

    @Operation(summary = "OAuth Login API", description = "OAuth Login API", tags = {"Auth"})
    @PostMapping("oauth/login")
    public BaseResponse<LoginResponseVO> oauthLogin(@RequestBody OauthLoginRequestVO oauthLoginRequestVO) {
        LoginResponseDTO loginResponseDTO = authService.oauthLogin(oauthLoginRequestVO.toDTO());
        return new BaseResponse<LoginResponseVO>(loginResponseDTO.toVO());
    }
    
    @Operation(security = @SecurityRequirement(name = "Bearer Auth")
    ,summary = "Get OAuth info List API", description = "OAuth info list API", tags = {"Auth"})
    @PostMapping("oauthinfo")
    public BaseResponse<OauthInfoResponseVO> oauthRegister(@AuthenticationPrincipal AuthUserDetail authUserDetail) {
        return new BaseResponse<>(authService.getOauthInfo(authUserDetail.getUsername()).toVO());
    }

}
