package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.dto.ChapterResponseDTO;
import com.ltweb2.spiderlily.entity.Chapter;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class ChapterController {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private BookRepository bookRepository;

    // --- HÀM 1: LẤY NỘI DUNG CHỮ CỦA 1 CHƯƠNG CỤ THỂ ---
    @GetMapping("/{bookId}/chapters/{chapterNumber}")
    public ResponseEntity<?> getChapterContent(@PathVariable Long bookId, @PathVariable Integer chapterNumber) {
        try {
            // Tìm kiếm chương hiện tại dựa vào bookId và chapterNumber
            Chapter chapter = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber)
                    .orElse(null);

            // 🌟 NẾU KHÔNG TÌM THẤY: Trả về mã lỗi 404 sạch sẽ kèm JSON thông báo thay vì
            // sập lỗi 500
            if (chapter == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Không tìm thấy nội dung chương truyện này trong cơ sở dữ liệu!\"}");
            }

            // Chuyển dữ liệu sang bản DTO để gửi về cho Frontend
            ChapterResponseDTO response = new ChapterResponseDTO();
            response.setBookId(chapter.getBookId());
            response.setChapterNumber(chapter.getChapterNumber());
            response.setTitle(chapter.getTitle());
            response.setContent(chapter.getContent());

            // Bạn có thể chỉnh sửa cứng tên hoặc lấy động từ Book entity nếu có liên kết
            response.setBookTitle("");

            // Kiểm tra xem có chương kế tiếp hay không để bật/tắt nút ở Frontend
            boolean hasNext = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber + 1).isPresent();
            response.setLast(!hasNext);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log lỗi chi tiết ra console backend để quản trị viên dễ debug
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Đã xảy ra lỗi hệ thống nghiêm trọng tại Server!\"}");
        }
    }

    // --- HÀM 2: LẤY TOÀN BỘ DANH SÁCH CHƯƠNG CỦA MỘT BỘ TRUYỆN ---
    @GetMapping("/{bookId}/chapters")
    public ResponseEntity<?> getAllChaptersByBook(@PathVariable Long bookId) {
        List<Chapter> chapters = chapterRepository.findByBookId(bookId);
        return ResponseEntity.ok(chapters);
    }

    // --- HÀM 3: XÓA MỘT CHƯƠNG TRUYỆN THEO ID ---
    @DeleteMapping("/chapters/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id) {
        if (!chapterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chapterRepository.deleteById(id);
        return ResponseEntity.ok().body("{\"message\": \"Xóa chương thành công!\"}");
    }

    // --- HÀM 4: THÊM CHƯƠNG MỚI CHO BỘ TRUYỆN CỤ THỂ ---
    @PostMapping("/{bookId}/chapters")
    public ResponseEntity<?> addChapter(@PathVariable Long bookId, @RequestBody Chapter chapter) {
        return bookRepository.findById(bookId).map(book -> {
            chapter.setBookId(book.getId());
            Chapter savedChapter = chapterRepository.save(chapter);
            return ResponseEntity.ok(savedChapter);
        }).orElse(ResponseEntity.notFound().build());
    }
}