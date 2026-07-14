package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.Comment;
import com.ltweb2.spiderlily.entity.User;
import com.ltweb2.spiderlily.repository.CommentRepository;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("*")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * ĐIỀU HƯỚNG GIAO DIỆN
     * URL: http://localhost:8080/api/comments/manage-comments.html
     */
    @GetMapping("/manage-comments.html")
    public ModelAndView showManageCommentsPage() {
        return new ModelAndView("manage-comments");
    }

    /**
     * API 1: Lấy bình luận theo Book ID
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Comment>> getCommentsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(commentRepository.findByBookIdOrderByCreatedAtDesc(bookId));
    }

    /**
     * API 2: Thêm bình luận mới (Chuẩn hóa theo Long ID)
     */
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> payload) {
        if (payload.get("userId") == null || payload.get("bookId") == null || payload.get("content") == null) {
            return ResponseEntity.badRequest().body("Thiếu dữ liệu truyền lên!");
        }

        // Ép kiểu an toàn từ dữ liệu JSON sang Long
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long bookId = Long.valueOf(payload.get("bookId").toString());
        String content = payload.get("content").toString();

        // Kiểm tra xem User và Book có tồn tại thực không
        boolean userExists = userRepository.existsById(userId);
        boolean bookExists = bookRepository.existsById(bookId);

        if (!userExists || !bookExists) {
            return ResponseEntity.badRequest().body("User hoặc Book không tồn tại trong hệ thống!");
        }

        // Tạo đối tượng và set trực tiếp ID dạng số (Khớp 100% với Entity Comment của
        // bạn)
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setBookId(bookId);
        comment.setContent(content);

        return ResponseEntity.ok(commentRepository.save(comment));
    }

    /**
     * API 3: Lấy TOÀN BỘ bình luận
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<Comment>> getAllCommentsForAdmin() {
        return ResponseEntity.ok(commentRepository.findAllByOrderByCreatedAtDesc());
    }

    /**
     * API 4: Xóa bình luận
     */
    @DeleteMapping("/admin/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            return ResponseEntity.badRequest().body("Bình luận không tồn tại hoặc đã bị xóa trước đó!");
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok("Xóa bình luận thành công!");
    }

    /**
     * API 5: Khóa/Mở khóa độc giả (Dùng trường số status: 1 active, 0 locked)
     */
    @PutMapping("/admin/user/{userId}/status")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long userId, @RequestParam boolean active) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Khớp với hàm setStatus(int) trong database thực tế của bạn
            user.setStatus(active ? 1 : 0);
            userRepository.save(user);

            String msg = active ? "Mở khóa tài khoản thành công!" : "Đã khóa tài khoản độc giả thành công!";
            return ResponseEntity.ok(msg);
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy tài khoản độc giả này!");
        }
    }
}