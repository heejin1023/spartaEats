package com.sparta.spartaeats.location.repository;

import com.sparta.spartaeats.location.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Page<Location> findByUseYn(Character useYn, Pageable pageable);
    Optional<Location> findByIdAndDelYn(UUID locationId, Character n);
}
