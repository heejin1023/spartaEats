package com.sparta.spartaeats.store.service;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.location.domain.Location;
import com.sparta.spartaeats.location.repository.LocationRepository;
import com.sparta.spartaeats.store.domain.Store;
import com.sparta.spartaeats.store.repository.StoreRepository;
import com.sparta.spartaeats.store.domain.StoreSpecification;
import com.sparta.spartaeats.store.dto.StoreRequestDto;
import com.sparta.spartaeats.store.dto.StoreResponseDto;
import com.sparta.spartaeats.store.dto.StoreSearchRequestDto;
import com.sparta.spartaeats.storeCategory.domain.StoreCategory;
import com.sparta.spartaeats.storeCategory.repository.StoreCategoryRepository;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {
    //UserRepository 등록
    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    // 음식점 등록
    public ApiResult createStore(StoreRequestDto storeRequestDto) {


        User user = userRepository.findByIdAndDelYn(storeRequestDto.getUserIdx(), 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 OWNER를 찾을 수 없습니다."));


        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(storeRequestDto.getCategoryId(), 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        Store store = Store.builder()
                .id(UUID.randomUUID())
                .owner(user)
                .storeName(storeRequestDto.getStoreName())
                .storeContact(storeRequestDto.getStoreContact())
                .storeAddress(storeRequestDto.getStoreAddress())
                .storeCategory(storeCategory)
                .useYn('Y')
                .delYn('N')
                .build();
        if (storeRequestDto.getLocationId() != null) {
            Location location = new Location();
            location = locationRepository.findByIdAndDelYn(storeRequestDto.getLocationId(), 'N')
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 지역을 찾을 수 없습니다."));
            store.setLocation(location);
        }
        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 등록 성공");
        return apiResult;
    }

    // 음식점 정보 수정
    public ApiResult updateStore(UUID storeId, StoreRequestDto storeRequestDto) {
        Store store = storeRepository.findByIdAndDelYn(storeId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

        if(storeRequestDto.getStoreName() != null){
            store.setStoreName(storeRequestDto.getStoreName());
        }
        if(storeRequestDto.getStoreContact() != null){
            store.setStoreContact(storeRequestDto.getStoreContact());
        }
        if(storeRequestDto.getStoreAddress() != null){
            store.setStoreAddress(storeRequestDto.getStoreAddress());
        }
        if(storeRequestDto.getCategoryId() != null){
            StoreCategory storeCategory = storeCategoryRepository.findById(storeRequestDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
            store.setStoreCategory(storeCategory);
        }
        if(storeRequestDto.getUseYn() != null){
            store.setUseYn(storeRequestDto.getUseYn());
        }
        if (storeRequestDto.getLocationId() != null) {
            Location location = new Location();
            location = locationRepository.findByIdAndDelYn(storeRequestDto.getLocationId(), 'N')
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 지역을 찾을 수 없습니다."));
            store.setLocation(location);
        }
        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 정보 수정 성공");
        return apiResult;
    }

    //음식점 삭제
    public ApiResult deleteStore(Long loginUserIdx, UUID storeId) {
        Store store = storeRepository.findByIdAndDelYn(storeId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        store.setDelYn('Y');
        store.setDeletedAt(LocalDateTime.now());
        store.setDeletedBy(loginUserIdx);
        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 정보 삭제 성공");
        return apiResult;
    }

    // 음식점 상세 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreDetail(UUID store_id) {
        Store store = storeRepository.findByIdAndDelYn(store_id, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

        StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                .storeId(store.getId())
                .userIdx(store.getOwner().getId())
                .storeName(store.getStoreName())
                .storeContact(store.getStoreContact())
                .storeAddress(store.getStoreAddress())
                .storeCategoryId(store.getStoreCategory().getId())
                .useYn(store.getUseYn())
                .createdAt(store.getCreatedAt())
                .createdBy(store.getCreatedBy())
                .modifiedAt(store.getModifiedAt())
                .modifiedBy(store.getModifiedBy())
                .build();
        return storeResponseDto;

    }

    // 음식점 전체 조회 및 검색
    @Transactional(readOnly = true)
    public Page<StoreResponseDto> getStores(StoreSearchRequestDto searchRequestDto, Pageable pageable) {
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if(searchRequestDto.getCategoryId() != null) {
            storeCategoryRepository.findByIdAndDelYn(searchRequestDto.getCategoryId(), 'N')
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리 ID 입력"));
        }
        if(searchRequestDto.getLocationId() != null) {
            locationRepository.findByIdAndDelYn(searchRequestDto.getLocationId(), 'N')
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 location_id 입력"));
        }

        Page<Store> storePage = storeRepository.findAll(StoreSpecification.searchWith(searchRequestDto, isAdmin), pageable);

        // Store 엔티티를 StoreResponseDto로 변환
        Page<StoreResponseDto> storeResponseDtoPage = storePage.map(store -> new StoreResponseDto(
                store.getId(),
                store.getOwner().getId(),
                store.getStoreName(),
                store.getStoreContact(),
                store.getStoreAddress(),
                store.getStoreCategory().getId(),  // StoreCategory 객체 대신 ID만 가져옴
                store.getLocation() != null ? store.getLocation().getId() : null,  // Location이 null이면 null 반환
                store.getUseYn(),
                store.getDelYn(),
                store.getCreatedAt(),
                store.getCreatedBy(),
                store.getModifiedAt(),
                store.getModifiedBy(),
                store.getDeletedAt(),
                store.getDeletedBy()
        ));
        return storeResponseDtoPage;
    }

    public boolean isOwnerOfStore(Long userIdx, UUID storeId) {
        // StoreRepository에서 storeId로 음식점 정보를 가져와서
        // 해당 음식점이 userId와 일치하는지 확인
        Store store = storeRepository.findByIdAndDelYn(storeId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

        StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                .storeId(store.getId())
                .userIdx(store.getOwner().getId())
                .build();

        return storeResponseDto != null && storeResponseDto.getUserIdx().equals(userIdx);
    }

    public UUID getIdByOwner(User user) {
        Store store = storeRepository.findByOwnerAndDelYn(user, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        return store.getId();
    }

}
