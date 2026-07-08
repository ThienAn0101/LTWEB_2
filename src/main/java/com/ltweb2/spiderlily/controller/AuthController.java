package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.User;
import com.ltweb2.spiderlily.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🌟 1. API ĐĂNG KÝ (Giữ nguyên vì bạn đã làm chuẩn)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Kiểm tra xem trùng tên tài khoản không
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tài khoản này đã tồn tại!"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Tự động thiết lập các giá trị mặc định khi tạo tài khoản mới
        user.setRole("USER"); // Mặc định là độc giả thường
        user.setActive(true); // Mặc định tài khoản được phép hoạt động
        user.setJoinDate(LocalDate.now()); // Lưu ngày đăng ký là ngày hôm nay

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!", "userId", savedUser.getId()));
    }

    // 🌟 2. API ĐĂNG NHẬP (ĐÃ SỬA LOGIC SO SÁNH)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password"); // Mật khẩu thô do người dùng nhập từ giao diện (VD: "123456")

        Optional<User> userOpt = userRepository.findByUsername(username);

        // 🔥 ĐOẠN ĐÃ SỬA: Sử dụng passwordEncoder.matches(mật_khẩu_thô,
        // mật_khẩu_đã_mã_hóa_ở_db)
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tài khoản hoặc mật khẩu không chính xác!"));
        }

        User user = userOpt.get();

        // Kiểm tra xem tài khoản có đang bị khóa (active = false) hay không
        if (!user.isActive()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "🔒 Tài khoản của bạn đã bị khóa! Vui lòng liên hệ Admin."));
        }

        // Trả về thông tin đăng nhập thành công cho Frontend
        return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công!",
                "userId", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFullName() != null ? user.getFullName() : "",
                "role", user.getRole()));
    }
}