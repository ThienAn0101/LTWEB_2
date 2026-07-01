package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Ai đọc?

    @Column(name = "book_id", nullable = false)
    private Long bookId; // Đọc bộ truyện nào?

    @Column(name = "chapter_number", nullable = false)
    private Integer chapterNumber; // Đang dừng chân ở chương số mấy?

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian đọc gần nhất

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now(); // Tự động cập nhật thời gian mỗi lần đọc tiếp
    }
}