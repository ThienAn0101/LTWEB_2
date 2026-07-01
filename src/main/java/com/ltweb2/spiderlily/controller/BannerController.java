package com.ltweb2.spiderlily.controller;

import com.ltweb2.spiderlily.entity.Banner;
import com.ltweb2.spiderlily.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*")
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    // Định nghĩa thư mục lưu trữ ảnh trong project (giống như phần lưu ảnh truyện
    // của bạn)
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/banners/";

    // 1. API Lấy tất cả danh sách banner
    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
        return ResponseEntity.ok(bannerRepository.findAll());
    }

    // 2. API NÂNG CẤP: Nhận file ảnh từ máy tính gửi lên
    @PostMapping
    public ResponseEntity<?> createBanner(
            @RequestParam("title") String title,
            @RequestParam("linkToBook") String linkToBook,
            @RequestParam("imageFile") MultipartFile file) {

        try {
            // Kiểm tra xem người dùng đã chọn file chưa
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"message\": \"Vui lòng chọn một file ảnh!\"}");
            }

            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Đổi tên file bằng chuỗi ngẫu nhiên UUID để tránh trùng tên file trên hệ thống
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;

            // Đường dẫn lưu file thực tế trên ổ đĩa
            Path path = Paths.get(UPLOAD_DIR + newFilename);
            Files.write(path, file.getBytes());

            // Lưu thông tin vào Database, đường dẫn web dẫn tới file ảnh sẽ là
            // /uploads/banners/tên_file
            Banner banner = new Banner();
            banner.setTitle(title);
            banner.setLinkToBook(linkToBook);
            banner.setImageUrl("/uploads/banners/" + newFilename);

            Banner savedBanner = bannerRepository.save(banner);
            return ResponseEntity.ok(savedBanner);

        } catch (IOException e) {
            e.getStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Lỗi khi lưu file ảnh!\"}");
        }
    }

    // 3. API Xóa một banner
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        if (!bannerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bannerRepository.deleteById(id);
        return ResponseEntity.ok().body("{\"message\": \"Xóa banner thành công!\"}");
    }
}