package com.ltweb2.spiderlily.repository;

import com.ltweb2.spiderlily.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}