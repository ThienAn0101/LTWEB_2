package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Lấy danh sách các sách có cùng categoryId nhưng loại trừ quyển hiện tại (đang xem)
    List<Book> findTop5ByCategoryIdAndIdNot(Long categoryId, Long currentBookId);
}