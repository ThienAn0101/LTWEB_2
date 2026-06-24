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

    private String fullName;

    private String email;

    private LocalDate joinDate;

    private boolean active = true; // true: Hoạt động, false: Bị khóa

    // --- CONSTRUCTOR ---
    public User() {
    }

    public User(String username, String fullName, String email, LocalDate joinDate, boolean active) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.joinDate = joinDate;
        this.active = active;
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
}