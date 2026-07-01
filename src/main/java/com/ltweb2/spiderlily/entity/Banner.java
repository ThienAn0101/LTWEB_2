package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
@Data
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl; // Đường dẫn ảnh banner

    @Column(name = "link_to_book")
    private String linkToBook; // Đường dẫn khi bấm vào banner sẽ nhảy tới bộ truyện (ví dụ:
                               // detail-book.html?id=5)

    @Column(name = "title")
    private String title; // Tiêu đề chương trình hoặc tên truyện trên banner

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Tự động lưu thời gian khi tạo banner
    }
}