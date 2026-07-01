package com.ltweb2.spiderlily;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ltweb2.spiderlily.entity.User;
import com.ltweb2.spiderlily.repository.UserRepository;

// Cấu hình quét tất cả các component, entity, repository nằm trong thư mục gốc com.ltweb2.spiderlily
@SpringBootApplication(scanBasePackages = "com.ltweb2.spiderlily")
public class SpiderlilyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderlilyApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User("nguyenvana", "Nguyễn Văn A", "vana@gmail.com", LocalDate.now(), true));
                userRepository.save(
                        new User("tranvanb", "Trần Văn B", "vanb@gmail.com", LocalDate.now().minusDays(5), false));
            }
        };
    }
}