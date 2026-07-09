package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.Book;
import com.ltweb2.spiderlily.entity.Category;
import com.ltweb2.spiderlily.repository.BookRepository;
import com.ltweb2.spiderlily.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 1. API Lấy danh sách tất cả các truyện (Đã tích hợp phân trang)
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        // Sắp xếp các truyện mới thêm (ID lớn hơn) lên trên đầu trang quản lý
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("id").descending());

        org.springframework.data.domain.Page<Book> bookPage = bookRepository.findAll(pageable);
        return ResponseEntity.ok(bookPage);
    }

    // 2. API Lấy chi tiết 1 cuốn truyện theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. API Thêm truyện mới (Đã chuẩn hóa)
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
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/uploads/";
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.copy(file.getInputStream(), path,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                book.setCoverUrl("/uploads/" + fileName);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Lỗi hệ thống khi lưu file ảnh: " + e.getMessage());
            }
        }

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // 4. API Sửa thông tin truyện hỗ trợ tải file ảnh
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @RequestPart("book") Book bookDetails,
            @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file,
            @RequestParam Long categoryId) {

        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy Danh mục truyện hợp lệ!");
        }

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setSummary(bookDetails.getSummary());
        book.setCategory(category);

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

                book.setCoverUrl("/uploads/" + fileName);
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