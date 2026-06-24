package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chapters")
@Data
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "chapter_number", nullable = false)
    private Integer chapterNumber;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Transient // Biến tạm không lưu vào database
    private String bookTitle;

    // === TỰ VIẾT TAY HÀM GETTER / SETTER ĐỂ KHỬ LỖI LOMBOK ===
    public String getBookTitle() {
        return this.bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    // =======================================================
}