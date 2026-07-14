package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Tìm các bình luận của một quyển sách theo bookId (Khớp hoàn toàn với Entity)
    List<Comment> findByBookIdOrderByCreatedAtDesc(Long bookId);

    // Lấy toàn bộ danh sách cho Admin
    List<Comment> findAllByOrderByCreatedAtDesc();
}