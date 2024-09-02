package com.sparta.spartaeats.storeCategory.service;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.storeCategory.domain.StoreCategory;
import com.sparta.spartaeats.storeCategory.repository.StoreCategoryRepository;
import com.sparta.spartaeats.storeCategory.domain.StoreCategorySpecification;
import com.sparta.spartaeats.storeCategory.dto.StoreCategoryRequestDto;
import com.sparta.spartaeats.storeCategory.dto.StoreCategoryResponseDto;
import com.sparta.spartaeats.storeCategory.dto.StoreCategorySearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreCategoryService {
    private final StoreCategoryRepository storeCategoryRepository;
//    private final JPAQueryFactory queryFactory;

    //카테고리 등록
    @Transactional
    public ApiResult createStoreCategory(StoreCategoryRequestDto storeCategoryRequestDto){
        StoreCategory storeCategory = StoreCategory.builder()
                .id(UUID.randomUUID())
                .categoryName(storeCategoryRequestDto.getCategoryName())
                .categoryDescription(storeCategoryRequestDto.getCategoryDescription())
                .delYn('N')
                .useYn('Y')
                .build();
        StoreCategory storeResponse = storeCategoryRepository.save(storeCategory);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 등록 성공");
        return apiResult;
    }

    //카테고리 정보 수정
    public ApiResult updateStoreCategory(UUID store_category_id, StoreCategoryRequestDto storeCategoryRequestDto){

        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(store_category_id, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        if(storeCategoryRequestDto.getCategoryName() != null){
            storeCategory.setCategoryName(storeCategoryRequestDto.getCategoryName());
        }
        if(storeCategoryRequestDto.getCategoryDescription() != null){
            storeCategory.setCategoryDescription(storeCategoryRequestDto.getCategoryDescription());
        }
        if(storeCategoryRequestDto.getUseYn() != null){
            storeCategory.setUseYn(storeCategoryRequestDto.getUseYn());
        }
        storeCategoryRepository.save(storeCategory);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 정보 수정 성공");
        return apiResult;
    }

    //카테고리 삭제
    public ApiResult deleteStoreCategory(Long loginUserIdx, UUID store_category_id) {
        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(store_category_id, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        storeCategory.setDelYn('Y');
        storeCategory.setDeletedAt(LocalDateTime.now());
        storeCategory.setDeletedBy(loginUserIdx);
        storeCategoryRepository.save(storeCategory);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 삭제 성공");
        return apiResult;

    }




    // 카테고리 상세 조회
    @Transactional(readOnly = true)
    public StoreCategory getCategoryDetails(UUID storeCategoryId) {
        StoreCategory storeCategory = storeCategoryRepository.findById(storeCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return storeCategory;
    }

    //카테고리 목록 조회 및 검색
    public Page<StoreCategoryResponseDto> getStoreCategories(StoreCategorySearchRequestDto storeCategorySearchRequestDto, Pageable pageable) {
        Page<StoreCategory> storeCategoryPage = storeCategoryRepository.findAll(StoreCategorySpecification.searchWith(storeCategorySearchRequestDto), pageable);
        // Store 엔티티를 StoreResponseDto로 변환
        Page<StoreCategoryResponseDto> storeCategoryResponseDto = storeCategoryPage.map(storeCategory -> new StoreCategoryResponseDto(
                storeCategory.getId(),
                storeCategory.getCategoryName(),
                storeCategory.getCategoryDescription(),
                storeCategory.getUseYn(),
                storeCategory.getDelYn(),
                storeCategory.getCreatedAt(),
                storeCategory.getCreatedBy(),
                storeCategory.getModifiedAt(),
                storeCategory.getModifiedBy(),
                storeCategory.getDeletedAt(),
                storeCategory.getDeletedBy()
        ));
        return storeCategoryResponseDto;
    }


}
