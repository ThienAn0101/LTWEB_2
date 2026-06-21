package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Để trống ở đây, Spring Boot đã tự làm sẵn các hàm CRUD (Thêm, Sửa, Xóa, Xem)
}