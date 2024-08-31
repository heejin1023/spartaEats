package com.sparta.spartaeats.ai.repository;

import com.sparta.spartaeats.ai.domain.AiArchive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiArchiveRepository extends JpaRepository<AiArchive, UUID> {
}
