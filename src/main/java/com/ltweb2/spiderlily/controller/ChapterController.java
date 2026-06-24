package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.dto.ChapterResponseDTO;
import com.ltweb2.spiderlily.entity.Chapter;
import com.ltweb2.spiderlily.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class ChapterController {

    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/{bookId}/chapters/{chapterNumber}")
    public ResponseEntity<?> getChapterContent(@PathVariable Long bookId, @PathVariable Integer chapterNumber) {

        // LỆNH 1: Tìm kiếm chính xác chương hiện tại để lấy nội dung chữ (Trả về đối
        // tượng Chapter)
        Chapter chapter = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nội dung chương!"));

        // Chuyển dữ liệu từ đối tượng "chapter" sang bản DTO để gửi về cho Frontend
        ChapterResponseDTO response = new ChapterResponseDTO();
        response.setBookId(chapter.getBookId());
        response.setChapterNumber(chapter.getChapterNumber());
        response.setTitle(chapter.getTitle());
        response.setContent(chapter.getContent());
        response.setBookTitle("Doraemon");

        // LỆNH 2: Kiểm tra xem có chương kế tiếp (chapterNumber + 1) hay không để
        // bật/tắt nút ở Frontend
        boolean hasNext = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber + 1).isPresent();

        // Nếu KHÔNG có chương kế tiếp (!hasNext) thì tức là đây là chương cuối (last =
        // true)
        response.setLast(!hasNext);

        return ResponseEntity.ok(response);
    }
}