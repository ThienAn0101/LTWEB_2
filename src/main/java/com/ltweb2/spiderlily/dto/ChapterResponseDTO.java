package com.ltweb2.spiderlily.dto;

import lombok.Data;

@Data
public class ChapterResponseDTO {
    private Long bookId;
    private Integer chapterNumber;
    private String title;
    private String content;
    private String bookTitle;
    private boolean last;

    // Hàm Setter thủ công viết chuẩn để khớp với Controller của bạn
    public void setLast(boolean last) {
        this.last = last;
    }

    // Hàm Getter thủ công viết chuẩn
    public boolean isLast() {
        return this.last;
    }
}