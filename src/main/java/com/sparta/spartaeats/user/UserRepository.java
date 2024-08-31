package com.sparta.spartaeats.user;

import com.sparta.spartaeats.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
