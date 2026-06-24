package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.dto.DashboardDTO;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.CategoryRepository;
import com.ltweb2.spiderlily.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stats")
    public DashboardDTO getDashboardStats() {
        long categories = categoryRepository.count(); // Đếm tổng số danh mục
        long books = bookRepository.count(); // Đếm tổng số truyện
        long users = userRepository.count(); // Đếm tổng số độc giả

        return new DashboardDTO(categories, books, users);
    }
}