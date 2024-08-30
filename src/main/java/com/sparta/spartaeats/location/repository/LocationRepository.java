package com.sparta.spartaeats.location.repository;

import com.sparta.spartaeats.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
