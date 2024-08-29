package com.sparta.spartaeats.user;

import com.sparta.spartaeats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
