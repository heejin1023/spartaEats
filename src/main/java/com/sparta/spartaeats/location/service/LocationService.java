package com.sparta.spartaeats.location.service;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.location.domain.Location;
import com.sparta.spartaeats.location.dto.LocationRequestDto;
import com.sparta.spartaeats.location.dto.LocationResponseDto;
import com.sparta.spartaeats.location.repository.LocationRepository;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationResponseDto createLocation(LocationRequestDto resquestDto, User user) {
        Location location = locationRepository.save(new Location(resquestDto, user));
        return new LocationResponseDto(location);
    }

    public LocationResponseDto updateLocation(UUID locationId, LocationRequestDto requestDto, User user) {
        Location location = findLocation(locationId);

        // 관리자 권한 확인
        if (!user.getUserRole().equals(UserRoleEnum.ADMIN.getAuthority())) {
            throw new IllegalArgumentException("해당 위치를 수정할 권한이 없습니다.");
        }
        // DTO에서 받은 데이터로 엔티티를 업데이트
        location.setLocationName(requestDto.getLocationName());
        location.setUseYn(requestDto.getUseYn());
        location.setDelYn(requestDto.getDelYn());

        // 업데이트된 엔티티를 저장
        Location updatedLocation = locationRepository.save(location);

        // 업데이트된 엔티티를 DTO로 변환하여 반환
        return new LocationResponseDto(updatedLocation);
    }


    public void deleteLocation(UUID locationId, User user) {
        Location location = findLocation(locationId);
        // 관리자 권한 확인
        if (!user.getUserRole().equals(UserRoleEnum.ADMIN.getAuthority())) {
            throw new IllegalArgumentException("해당 위치를 삭제할 권한이 없습니다.");
        }
        location.setDelYn('Y');
        locationRepository.save(location);

    }


    public List<LocationResponseDto> getAllLocations(Pageable pageable) {
        List<Location> locations;
        locations = locationRepository.findAll(pageable).getContent();
        return locations.stream()
                .map(LocationResponseDto::new) // 생성자에서 Location 객체를 받는 LocationResponseDto가 필요
                .collect(Collectors.toList());
    }

    // Address 조회 및 예외 처리
    private Location findLocation(UUID locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주소지가 없습니다. "));
    }

}
