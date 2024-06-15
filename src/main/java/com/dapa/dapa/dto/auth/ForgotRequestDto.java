package com.dapa.dapa.dto.auth;

import lombok.*;

@Data
@AllArgsConstructor
public class ForgotRequestDto {
    String oneTimePassword;

    String newPassword;
}
