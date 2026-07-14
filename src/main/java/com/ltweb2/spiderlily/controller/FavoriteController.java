package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.Favorite;
import com.ltweb2.spiderlily.repository.FavoriteRepository;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*") // Tránh lỗi CORS khi kết nối với Frontend
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // 1. Lấy danh sách yêu thích của User: GET /api/favorites/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFavoritesByUser(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    // 2. Thêm vào yêu thích (Bấm Tim): POST /api/favorites
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long bookId = payload.get("bookId");

        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().body("Thiếu thông tin userId hoặc bookId!");
        }

        // Kiểm tra xem đã thích chưa để tránh trùng lặp dữ liệu
        if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            return ResponseEntity.badRequest().body("Truyện này đã được yêu thích rồi!");
        }

        // Kiểm tra sự tồn tại của User và Book trong DB trước khi lưu
        boolean userExists = userRepository.existsById(userId);
        boolean bookExists = bookRepository.existsById(bookId);

        if (!userExists || !bookExists) {
            return ResponseEntity.badRequest().body("Không tìm thấy thông tin User hoặc Book tương ứng!");
        }

        // Tạo thực thể Favorite mới và gán trực tiếp ID (Không còn bị báo đỏ nữa!)
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBookId(bookId);

        favoriteRepository.save(favorite);
        return ResponseEntity.ok("Đã thêm vào danh sách yêu thích!");
    }

    // 3. Xóa yêu thích (Bỏ Tim): DELETE /api/favorites
    @DeleteMapping
    public ResponseEntity<?> removeFavorite(@RequestParam Long userId, @RequestParam Long bookId) {
        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().body("Thiếu thông tin userId hoặc bookId!");
        }

        favoriteRepository.deleteByUserIdAndBookId(userId, bookId);
        return ResponseEntity.ok("Đã xóa khỏi danh sách yêu thích!");
    }
}