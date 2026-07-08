package com.ltweb2.spiderlily;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ltweb2.spiderlily.entity.User;
import com.ltweb2.spiderlily.repository.UserRepository;

@SpringBootApplication(scanBasePackages = "com.ltweb2.spiderlily")
public class SpiderlilyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderlilyApplication.class, args);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Tiêm bộ mã hóa vào

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {

                // Khởi tạo User A dùng các hàm set từng thuộc tính một
                User userA = new User();
                userA.setUsername("nguyenvana");
                userA.setFullName("Nguyễn Văn A");
                userA.setEmail("vana@gmail.com");
                userA.setJoinDate(LocalDate.now().minusDays(1));
                userA.setPassword(passwordEncoder.encode("123456"));
                userA.setRole("USER");

                // 🔥 ĐÃ SỬA: Đổi từ số 1 sang true
                userA.setActive(true);
                userRepository.save(userA);

                // Khởi tạo User B tương tự
                User userB = new User();
                userB.setUsername("tranvanb");
                userB.setFullName("Trần Văn B");
                userB.setEmail("vanb@gmail.com");
                userB.setJoinDate(LocalDate.now().minusDays(5));
                userB.setPassword(passwordEncoder.encode("123456"));
                userB.setRole("USER");

                // 🔥 ĐÃ SỬA: Đổi từ số 0 sang false
                userB.setActive(false);
                userRepository.save(userB);
            }
        };
    }
}