package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.Book;
import com.ltweb2.spiderlily.entity.Category;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. API Lấy danh sách tất cả các truyện
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 2. API Lấy chi tiết 1 cuốn truyện theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. API Thêm truyện mới (Đã chuẩn hóa)
    // Thay thế hàm createBook cũ bằng hàm này:
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createBook(
            @RequestPart("book") Book book,
            @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file,
            @RequestParam Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy Danh mục truyện hợp lệ!");
        }
        book.setCategory(category);

        // Xử lý lưu file ảnh vào máy tính nếu người dùng có chọn tệp
        if (file != null && !file.isEmpty()) {
            try {
                // Tạo tên file duy nhất tránh trùng lặp bằng cấu trúc thời gian hệ thống
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Thư mục lưu trữ ảnh tĩnh trong Spring Boot
                String uploadDir = "src/main/resources/static/uploads/";
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // Tự động tạo thư mục uploads nếu chưa có
                }

                // Thực hiện lưu tệp vật lý
                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(file.getInputStream(), path,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn tương đối vào Database để Front-end gọi hiển thị
                book.setCoverUrl("/uploads/" + fileName);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Lỗi hệ thống khi lưu file ảnh: " + e.getMessage());
            }
        }

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // 4. API Sửa thông tin truyện hỗ trợ tải file ảnh (Thay thế hàm cũ)
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @RequestPart("book") Book bookDetails,
            @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file,
            @RequestParam Long categoryId) {

        // 1. Kiểm tra truyện tồn tại không
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. Kiểm tra danh mục hợp lệ không
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy Danh mục truyện hợp lệ!");
        }

        // 3. Cập nhật các trường thông tin dạng text
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setSummary(bookDetails.getSummary());
        book.setCategory(category);

        // 4. Nếu người dùng chọn file ảnh mới thì mới ghi đè, nếu bỏ trống thì giữ ảnh
        // cũ
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/uploads/";

                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists())
                    dir.mkdirs();

                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(file.getInputStream(), path,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                book.setCoverUrl("/uploads/" + fileName); // Cập nhật đường dẫn ảnh mới
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Lỗi lưu file ảnh khi sửa: " + e.getMessage());
            }
        }

        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    // 5. API Xóa truyện
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id).map(book -> {
            bookRepository.delete(book);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}