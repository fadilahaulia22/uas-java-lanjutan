package com.dapa.dapa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dapa.dapa.constant.RolesConstant;
import com.dapa.dapa.exception.CustomAccessDeniedException;
import com.dapa.dapa.exception.CustomUnAuthorizeException;
import com.dapa.dapa.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        JwtFilter filter;

        @Bean
        PasswordEncoder getPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(new CustomUnAuthorizeException())
                                                .accessDeniedHandler(new CustomAccessDeniedException()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/auth/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/customer/register",
                                                                "/customer/upload-customer-photo",
                                                                "/product/get",
                                                                "/category/get")
                                                .permitAll()
                                                .requestMatchers(
                                                                "/cart/**",
                                                                "/report",
                                                                "/transaction/post")
                                                .hasAnyAuthority(RolesConstant.CUSTOMER_ROLE)
                                                .requestMatchers(
                                                                "/product/**",
                                                                "/category/**")
                                                .hasAnyAuthority(RolesConstant.WAREHOUSE_ROLE)
                                                .requestMatchers("/transaction/{id}","/transaction/view")
                                                .hasAuthority(RolesConstant.ACCOUNTANT_ROLE)
                                                .anyRequest().authenticated())
                                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}