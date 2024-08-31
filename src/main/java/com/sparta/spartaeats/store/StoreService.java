package com.sparta.spartaeats.store;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.store.dto.StoreRequestDto;
import com.sparta.spartaeats.store.dto.StoreResponseDto;
import com.sparta.spartaeats.store.dto.StoreSearchRequestDto;
import com.sparta.spartaeats.storeCategory.StoreCategory;
import com.sparta.spartaeats.storeCategory.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
//    private final UserRepository userRepository;

    // 음식점 등록
    public ApiResult createStore(StoreRequestDto storeRequestDto) {


//        User user = userRepository.findByIdAndDelYn(storeRequestDto.getUserIdx(), 'N')
//                .orElseThrow(() -> new IllegalArgumentException("해당 OWNER를 찾을 수 없습니다."));


        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(storeRequestDto.getCategoryId(), "N")
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        Store store = Store.builder()
                .id(UUID.randomUUID())
//                .owner(user)
                .storeName(storeRequestDto.getStoreName())
                .storeContact(storeRequestDto.getStoreContact())
                .storeAddress(storeRequestDto.getStoreAddress())
                .storeCategory(storeCategory)
//                .location(new Location(storeRequestDto.getLocationId())) //locationRepository.findById 로 변경 필요
                .useYn("Y")
                .delYn("N")
                .build();

        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 등록 성공");
        return apiResult;
    }

    // 음식점 정보 수정
    public ApiResult updateStore(UUID storeId, StoreRequestDto storeRequestDto) {
        Store store = storeRepository.findByIdAndDelYn(storeId, "N")
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

        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 정보 수정 성공");
        return apiResult;
    }

    //음식점 삭제
    public ApiResult deleteStore(UUID storeId) {
        Store store = storeRepository.findByIdAndDelYn(storeId, "N")
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        store.setDelYn("Y");
        store.setDeletedAt(LocalDateTime.now());
        storeRepository.save(store);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "음식점 정보 삭제 성공");
        return apiResult;
    }

    // 음식점 상세 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreDetail(UUID store_id) {
        Store store = storeRepository.findByIdAndDelYn(store_id, "N")
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));

        StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                .storeId(store.getId())
                .owner(store.getOwner())
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
        System.out.println(searchRequestDto.getUseYn());
        Page<Store> storePage = storeRepository.findAll(StoreSpecification.searchWith(searchRequestDto), pageable);

        // Store 엔티티를 StoreResponseDto로 변환
        Page<StoreResponseDto> storeResponseDtoPage = storePage.map(store -> new StoreResponseDto(
                store.getId(),
                store.getOwner(),
                store.getStoreName(),
                store.getStoreContact(),
                store.getStoreAddress(),
                store.getStoreCategory().getId(),  // StoreCategory 객체 대신 ID만 가져옴
                store.getUseYn(),
                store.getCreatedAt(),
                store.getCreatedBy(),
                store.getModifiedAt(),
                store.getModifiedBy(),
                store.getDeletedAt(),
                store.getDeletedBy()
        ));
        return storeResponseDtoPage;
    }


}
