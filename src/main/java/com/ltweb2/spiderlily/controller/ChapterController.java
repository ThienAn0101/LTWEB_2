package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.dto.ChapterResponseDTO;
import com.ltweb2.spiderlily.entity.Chapter;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books") // Đường dẫn gốc cho toàn bộ class là /api/books
@CrossOrigin(origins = "*")
public class ChapterController {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private BookRepository bookRepository; // Gom toàn bộ Autowired lên đầu cho sạch sẽ

    // --- HÀM 1: LẤY NỘI DUNG CHỮ CỦA 1 CHƯƠNG CỤ THỂ (Dành cho giao diện đọc
    // truyện của Độc giả) ---
    // Đường dẫn thực tế: GET
    // http://localhost:8080/api/books/{bookId}/chapters/{chapterNumber}
    @GetMapping("/{bookId}/chapters/{chapterNumber}")
    public ResponseEntity<?> getChapterContent(@PathVariable Long bookId, @PathVariable Integer chapterNumber) {

        // Tìm kiếm chương hiện tại để lấy nội dung chữ
        Chapter chapter = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nội dung chương!"));

        // Chuyển dữ liệu sang bản DTO để gửi về cho Frontend
        ChapterResponseDTO response = new ChapterResponseDTO();
        response.setBookId(chapter.getBookId());
        response.setChapterNumber(chapter.getChapterNumber());
        response.setTitle(chapter.getTitle());
        response.setContent(chapter.getContent());
        response.setBookTitle("Doraemon");

        // Kiểm tra xem có chương kế tiếp (chapterNumber + 1) hay không để bật/tắt nút ở
        // Frontend
        boolean hasNext = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber + 1).isPresent();

        // Nếu KHÔNG có chương kế tiếp (!hasNext) thì tức là đây là chương cuối (last =
        // true)
        response.setLast(!hasNext);

        return ResponseEntity.ok(response);
    }

    // --- HÀM 2: LẤY TOÀN BỘ DANH SÁCH CHƯƠNG CỦA MỘT BỘ TRUYỆN (Dành cho trang
    // quản lý manage-chapters) ---
    // Đường dẫn thực tế: GET http://localhost:8080/api/books/{bookId}/chapters
    @GetMapping("/{bookId}/chapters")
    public ResponseEntity<?> getAllChaptersByBook(@PathVariable Long bookId) {
        // Tìm tất cả các chương dựa vào bookId
        List<Chapter> chapters = chapterRepository.findByBookId(bookId);
        return ResponseEntity.ok(chapters);
    }

    // --- HÀM 3: XÓA MỘT CHƯƠNG TRUYỆN THEO ID ---
    // Đường dẫn thực tế: DELETE http://localhost:8080/api/books/chapters/{id}
    @DeleteMapping("/chapters/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id) {
        if (!chapterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chapterRepository.deleteById(id);
        return ResponseEntity.ok().body("{\"message\": \"Xóa chương thành công!\"}");
    }

    // --- HÀM 4: API thêm chương mới cho một bộ truyện cụ thể
    @PostMapping("/{bookId}/chapters")
    public ResponseEntity<?> addChapter(@PathVariable Long bookId, @RequestBody Chapter chapter) {

        // Kiểm tra xem bộ truyện (Book) có tồn tại trong Database hay không
        return bookRepository.findById(bookId).map(book -> {

            // 🌟 SỬA DÒNG NÀY: Thay vì truyền cả đối tượng 'book',
            // bạn chỉ cần truyền ID số của bộ truyện (.getId()) vào hàm setBookId()
            chapter.setBookId(book.getId());
            // Lưu xuống database thông qua chapterRepository
            Chapter savedChapter = chapterRepository.save(chapter);
            return ResponseEntity.ok(savedChapter);
        }).orElse(ResponseEntity.notFound().build());
    }
}