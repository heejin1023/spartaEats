package com.sparta.spartaeats.user.repository;

import com.sparta.spartaeats.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);
    User findByUserName(String userName);

    Optional<User> findByIdAndDelYn(Long userIdx, char delYn);
}