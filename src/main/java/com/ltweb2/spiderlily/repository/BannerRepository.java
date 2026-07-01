package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    // Kế thừa JpaRepository là đủ để dùng các hàm save(), findAll(),
    // deleteById()...
}