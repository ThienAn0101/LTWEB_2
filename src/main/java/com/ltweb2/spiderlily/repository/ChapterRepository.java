package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Sửa "class" thành "interface" và thêm "extends JpaRepository<Chapter, Long>"
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    // Tìm chính xác chương dựa vào mã sách và số thứ tự chương
    Optional<Chapter> findByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);
}