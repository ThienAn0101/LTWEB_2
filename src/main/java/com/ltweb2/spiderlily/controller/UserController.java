package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.User;
import com.ltweb2.spiderlily.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. API Lấy toàn bộ danh sách độc giả
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. API Khóa / Mở khóa độc giả độc quyền admin
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(!user.isActive()); // Đảo trạng thái active (true thành false và ngược lại)
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }
}