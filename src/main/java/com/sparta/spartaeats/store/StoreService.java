package com.sparta.spartaeats.store;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.store_category.StoreCategory;
import com.sparta.spartaeats.store_category.StoreCategoryRepository;
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

    // 음식점 등록
    public ApiResult createStore(StoreRequestDto storeRequestDto) {
//        User user = new User();
//        user.setId(storeRequestDto.getUserIdx());
//        user 정보 -> 체크 필요

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

        StoreCategory storeCategory = (StoreCategory) storeCategoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        store.setStoreName(storeRequestDto.getStoreName());
        store.setStoreContact(storeRequestDto.getStoreContact());
        store.setStoreAddress(storeRequestDto.getStoreAddress());
        store.setStoreCategory(storeCategory);
        store.setUseYn(storeRequestDto.getUseYn());
//        store.setUpdatedAt(LocalDateTime.now());
//        store.setUpdatedBy(storeRequestDto.getUserIdx());

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

        Page<Store> storePage = storeRepository.findAll(StoreSpecification.searchWith(searchRequestDto), pageable);

        // Store 엔티티를 StoreResponseDto로 변환
        Page<StoreResponseDto> storeResponseDtoPage = storePage.map(store -> new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getStoreContact(),
                store.getStoreAddress(),
                store.getStoreCategory().getId()  // StoreCategory 객체 대신 ID만 가져옴
        ));



        return storeResponseDtoPage;
//
//        Page<Store> stores = storeRepository.findAll(StoreSpecification.searchWith(searchRequestDto), pageable);
//
//        List<StoreListResponseDto> storeList = stores.stream()
//                .map(store -> StoreListResponseDto.builder()
//                        .storeId(store.getId())
//                        .storeName(store.getStoreName())
//                        .categoryName(store.getStoreCategory().getCategoryName())
//                        .build())
//                .collect(Collectors.toList());
//
//        PageInfo pageInfo = new PageInfo(stores.getTotalElements(), stores.getSize(), stores.getNumber(), stores.getTotalPages(), stores.hasPrevious(), stores.hasNext());
//        apiResult.set(ApiResultError.NO_ERROR).setList(storeList).setPageInfo(pageInfo);
        //return new ApiResult(200, "음식점 목록 조회 성공", new StoreListResultData(storeList, pageInfo));
    }


}
