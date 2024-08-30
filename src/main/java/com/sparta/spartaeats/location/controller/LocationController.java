package com.sparta.spartaeats.location.controller;

import com.sparta.spartaeats.location.dto.LocationRequestDto;
import com.sparta.spartaeats.location.dto.LocationResponseDto;
import com.sparta.spartaeats.location.entity.Location;
import com.sparta.spartaeats.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store_areas")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public LocationResponseDto createLocation(@RequestBody LocationRequestDto requestDto) {
        return locationService.createLocation(requestDto);
    }

    @PatchMapping("/{locationId}")
    public LocationResponseDto updateLocation(@PathVariable UUID locationId, @RequestBody LocationRequestDto requestDto) {
        return locationService.updateLocation(locationId, requestDto);
    }

    @DeleteMapping("/{locationId}/delete")
    public void deleteLocation(@PathVariable UUID locationId) {
        locationService.deleteLocation(locationId);
    }

    @GetMapping
    public List<LocationResponseDto> getAllLocations() {
        return locationService.getAllLocations();
    }
}
