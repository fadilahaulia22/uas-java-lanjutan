package com.dapa.dapa.service.auth;

import com.dapa.dapa.dto.auth.ForgotRequestDto;
import com.dapa.dapa.dto.auth.LoginRequestDto;
import com.dapa.dapa.dto.auth.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto dto);

    void sendEmailResetPasswordOtp(String email);

    void resetPassword(String email, ForgotRequestDto dto);
}
