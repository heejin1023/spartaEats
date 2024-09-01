package com.sparta.spartaeats.storeCategory.controller;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.storeCategory.domain.StoreCategory;
import com.sparta.spartaeats.storeCategory.domain.validationGroup.ValidStoreCategory001;
import com.sparta.spartaeats.storeCategory.service.StoreCategoryService;
import com.sparta.spartaeats.storeCategory.dto.StoreCategoryRequestDto;
import com.sparta.spartaeats.storeCategory.dto.StoreCategoryResponseDto;
import com.sparta.spartaeats.storeCategory.dto.StoreCategorySearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store_categories")
public class StoreCategoryController {
    private final StoreCategoryService storeCategoryService;

    //카테고리 등록

    @PostMapping
    @Secured(UserRoleEnum.Authority.ADMIN)
    public ApiResult createStoreCategory(@RequestBody @Validated(ValidStoreCategory001.class) StoreCategoryRequestDto storeCategoryRequestDto) {
        return storeCategoryService.createStoreCategory(storeCategoryRequestDto);
    }

    //카테고리 정보 수정
    @PatchMapping("/update")
    @Secured(UserRoleEnum.Authority.ADMIN)
    public ApiResult updateStoreCategory(@RequestParam(name = "store_category_id") UUID id, @RequestBody StoreCategoryRequestDto storeCategoryRequestDto) {
        return storeCategoryService.updateStoreCategory(id, storeCategoryRequestDto);
    }

    //카테고리 삭제
    @PatchMapping("/delete")
    @Secured(UserRoleEnum.Authority.ADMIN)
    public ApiResult deleteStoreCategory(@RequestParam(name = "store_category_id") UUID id) {
        return storeCategoryService.deleteStoreCategory(id);
    }

    //카테고리 상세 조회
    @GetMapping("/{store_category_id}")
    public StoreCategory getCategoryDetails(@PathVariable("store_category_id") UUID id) {
        return storeCategoryService.getCategoryDetails(id);
    }

    //카테고리 전체 조회 및 검색
    @GetMapping
    public ApiResult getStoreCategories(
            @RequestParam(value = "category_name", required = false) String category_name,
            @RequestParam(value = "use_yn", required = false) String use_yn,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "categoryName") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
                               ) {

        StoreCategorySearchRequestDto storeCategorySearchRequestDto = new StoreCategorySearchRequestDto(category_name, use_yn);
        // 정렬 순서 및 방향 설정
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortOption = Sort.by(sortDirection, sort);

        Pageable pageable = PageRequest.of(page - 1, size, sortOption);
        Page<StoreCategoryResponseDto> storeCategoryPage = storeCategoryService.getStoreCategories(storeCategorySearchRequestDto, pageable);
        // PageInfo 설정
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "카테고리 조회 성공");
        result.setList(storeCategoryPage);  // Page 데이터를 ApiResult에 설정
        result.setPageInfo(storeCategoryPage);  // PageInfo를 설정
        return result;
    }


}
