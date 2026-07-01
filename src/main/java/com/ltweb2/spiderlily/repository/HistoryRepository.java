package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    // Tìm kiếm lịch sử đọc theo userId và bookId
    Optional<History> findByUserIdAndBookId(Long userId, Long bookId);
}