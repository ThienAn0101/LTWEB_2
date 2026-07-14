package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Lấy danh sách yêu thích của một User
    List<Favorite> findByUserId(Long userId);

    // Kiểm tra xem đã tồn tại yêu thích này chưa
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    // Xóa yêu thích dựa trên userId và bookId khi bấm bỏ tim
    @Transactional
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}