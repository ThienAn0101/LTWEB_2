package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Người bình luận

    @Column(name = "book_id", nullable = false)
    private Long bookId; // Bình luận ở bộ truyện nào

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Nội dung chữ của bình luận

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}