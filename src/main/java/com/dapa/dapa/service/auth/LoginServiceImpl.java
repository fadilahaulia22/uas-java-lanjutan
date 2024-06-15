package com.dapa.dapa.service.auth;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dapa.dapa.dto.auth.ForgotRequestDto;
import com.dapa.dapa.dto.auth.LoginRequestDto;
import com.dapa.dapa.dto.auth.LoginResponseDto;
import com.dapa.dapa.entity.Users;
import com.dapa.dapa.repository.UserRepository;
import com.dapa.dapa.service.EmailService;
import com.dapa.dapa.utils.JwtUtil;

import jakarta.mail.MessagingException;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired 
    EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        Users user = userRepository
        .findByUsername(dto.getUsername())
        .orElse(null);
        System.out.println(user);
        if (user != null) {
            boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getPassword());
            System.out.println(isMatch);
            if (isMatch) {
                LoginResponseDto response = new LoginResponseDto();
                response.setUsername(user.getUsername());
                response.setRole(user.getRoles().getRoleName());
                response.setToken(jwtUtil.generateToken(user));
                return response;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
    }

    @Override
    public void sendEmailResetPasswordOtp(String email) {
        
        try {
            
            Users user = userRepository.findByUsername(email).orElse(null);
            generateOtp(user);
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    
    private void generateOtp(Users users) throws UnsupportedEncodingException, MessagingException {

        String OTP = RandomStringUtils.randomAlphanumeric(5);
        String encodedOTP = passwordEncoder.encode(OTP);
        
        users.setOtp(encodedOTP);
        users.setOtpReqTime(new Date());

        userRepository.save(users);
        emailService.sendOTPEmail(users.getUsername(), OTP);
    }

    @Override
    public void resetPassword(String email, ForgotRequestDto dto) {
        
        try {
            
            Users users = userRepository.findByUsername(email).orElse(null);
            if(passwordEncoder.matches(dto.getOneTimePassword(),users.getOtp()) && users.isOtpRequired()) {
                users.setOtp(null);
                users.setOtpReqTime(null);
                users.setPassword(passwordEncoder.encode(dto.getNewPassword()));
                userRepository.save(users);
            }
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP was expired");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
