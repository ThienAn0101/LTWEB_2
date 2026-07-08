package com.ltweb2.spiderlily.config; // Đã đổi theo đúng package dự án của bạn

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Khai báo công cụ mã hóa BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình cho phép các trang HTML tĩnh và API chạy tự do không bị chặn form
    // đăng nhập mặc định
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF để Frontend (Fetch API) dễ gọi
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Cho phép tất cả các request/API truy cập tự do
                );
        return http.build();
    }
}