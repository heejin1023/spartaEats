package com.sparta.spartaeats.location.repository;

import com.sparta.spartaeats.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByIdAndDelYn(UUID locationId, Character n);
}
