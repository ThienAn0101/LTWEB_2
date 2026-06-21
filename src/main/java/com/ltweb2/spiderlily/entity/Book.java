package com.ltweb2.spiderlily.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // --- CONSTRUCTOR ---
    public Book() {}

    public Book(String title, String author, String coverUrl, String summary, Category category) {
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.summary = summary;
        this.category = category;
    }

    // --- GETTERS AND SETTERS (Bắt buộc phải chính xác từng chữ) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}