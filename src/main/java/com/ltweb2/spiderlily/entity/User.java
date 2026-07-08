package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // 🌟 1. BỔ SUNG: Mật khẩu đăng nhập
    @Column(nullable = false)
    private String password;

    private String fullName;

    private String email;

    private LocalDate joinDate;

    private Boolean active = true; // true: Hoạt động, false: Bị khóa

    // 🌟 2. BỔ SUNG: Vai trò tài khoản ("USER" hoặc "ADMIN")
    @Column(nullable = false)
    private String role = "USER";

    // --- CONSTRUCTOR ---
    public User() {
    }

    public User(String username, String password, String fullName, String email, LocalDate joinDate, boolean active,
            String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.joinDate = joinDate;
        this.active = active;
        this.role = role;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // 🌟 Getter/Setter cho Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // 🌟 Getter/Setter cho Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}