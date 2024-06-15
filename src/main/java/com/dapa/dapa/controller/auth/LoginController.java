package com.dapa.dapa.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.dto.auth.ForgotRequestDto;
import com.dapa.dapa.dto.auth.LoginRequestDto;
import com.dapa.dapa.dto.auth.LoginResponseDto;
import com.dapa.dapa.service.auth.LoginService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto dto) {
        try {
            System.out.println(dto);
            LoginResponseDto response = loginService.login(dto);
            return ResponseEntity.ok().body(GenericResponse.success(response, "succesfully login!"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {

        try {

            loginService.sendEmailResetPasswordOtp(email);

            return ResponseEntity.ok()
                    .body(GenericResponse.success(null, "Successfully Send OTP to email"));
        } catch (ResponseStatusException e) {

            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode())
                    .body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String email, @RequestBody ForgotRequestDto dto) {

        try {
            loginService.resetPassword(email, dto);
            return ResponseEntity.ok()
                    .body(GenericResponse.success(null, "Successfully Changed The Password, Try Login"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode())
                    .body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
