package com.sparta.spartaeats.order.repository;

import com.sparta.spartaeats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
