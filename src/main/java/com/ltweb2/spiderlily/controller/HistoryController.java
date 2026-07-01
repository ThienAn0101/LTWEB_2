package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.History;
import com.ltweb2.spiderlily.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/histories")
@CrossOrigin(origins = "*")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;

    // 🌟 API 1: Lưu hoặc Cập nhật lịch sử đọc truyện
    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdateHistory(
            @RequestParam("userId") Long userId,
            @RequestParam("bookId") Long bookId,
            @RequestParam("chapterNumber") Integer chapterNumber) {

        // Kiểm tra xem cặp bài trùng (userId và bookId) đã tồn tại trong DB chưa
        Optional<History> existingHistory = historyRepository.findByUserIdAndBookId(userId, bookId);

        if (existingHistory.isPresent()) {
            // NẾU ĐÃ CÓ: Lấy bản ghi cũ ra và cập nhật số chương mới
            History history = existingHistory.get();
            history.setChapterNumber(chapterNumber);
            // Hàm @PreUpdate ở Entity sẽ tự động cập nhật lại thời gian updatedAt
            historyRepository.save(history);
            return ResponseEntity.ok("{\"message\": \"Đã cập nhật lịch sử đọc!\"}");
        } else {
            // NẾU CHƯA CÓ: Tạo mới hoàn toàn một dòng lịch sử
            History newHistory = new History();
            newHistory.setUserId(userId);
            newHistory.setBookId(bookId);
            newHistory.setChapterNumber(chapterNumber);

            historyRepository.save(newHistory);
            return ResponseEntity.ok("{\"message\": \"Đã tạo mới lịch sử đọc!\"}");
        }
    }

    // 🌟 API 2: Lấy thông tin lịch sử của 1 bộ truyện cụ thể (Để làm nút "Đọc
    // Tiếp")
    @GetMapping("/check")
    public ResponseEntity<?> checkHistory(
            @RequestParam("userId") Long userId,
            @RequestParam("bookId") Long bookId) {

        return historyRepository.findByUserIdAndBookId(userId, bookId)
                .map(history -> ResponseEntity.ok(history))
                .orElse(ResponseEntity.notFound().build());
    }
}