package com.sparta.spartaeats.user.repository;

import com.sparta.spartaeats.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);

    User findById(long id);

    @Query("SELECT u FROM User u " +
            "WHERE (:userName IS NULL OR TRIM(:userName) = '' OR LOWER(u.userName) LIKE LOWER(CONCAT('%', TRIM(:userName), '%'))) " +
            "AND (:userContact IS NULL OR TRIM(:userContact) = '' OR LOWER(u.userContact) LIKE LOWER(CONCAT('%', TRIM(:userContact), '%'))) " +
            "AND (:userEmail IS NULL OR TRIM(:userEmail) = '' OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%', TRIM(:userEmail), '%')))")
    Page<User> searchUsers(@Param("userName") String userName,
                           @Param("userContact") String userContact,
                           @Param("userEmail") String userEmail,
                           Pageable pageable);

    User findByUserEmail(String email);

    Optional<User> findByIdAndDelYn(Long userIdx, char delYn);
}