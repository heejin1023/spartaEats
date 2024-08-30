package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.store.StoreResponseDto;
import com.sparta.spartaeats.store.StoreSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store_categories")
public class StoreCategoryController {
    private final StoreCategoryService storeCategoryService;

    //카테고리 등록

    @PostMapping
    public ApiResult createStoreCategory(@RequestBody StoreCategoryRequestDto storeCategoryRequestDto) {
        return storeCategoryService.createStoreCategory(storeCategoryRequestDto);
    }

    //카테고리 정보 수정
    @PatchMapping("/update")
    public ApiResult updateStoreCategory(@RequestParam UUID store_category_id, @RequestBody StoreCategoryRequestDto storeCategoryRequestDto) {
        return storeCategoryService.updateStoreCategory(store_category_id, storeCategoryRequestDto);
    }

    //카테고리 삭제
    @PatchMapping("/delete")
    public ApiResult deleteStoreCategory(@RequestParam UUID store_category_id) {
        return storeCategoryService.deleteStoreCategory(store_category_id);
    }

    //카테고리 상세 조회
    @GetMapping("/{store_id}")
    public StoreCategory getCategoryDetails(@PathVariable("store_category_id") UUID store_category_id) {
        return storeCategoryService.getCategoryDetails(store_category_id);
    }

    //카테고리 전체 조회 및 검색
    @GetMapping
    public ApiResult getStoreCategories(
            @RequestParam(value = "category_name", required = false) String category_name,
            @RequestParam(value = "use_yn", required = false) String use_yn,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
                               ) {

        StoreCategorySearchRequestDto storeCategorySearchRequestDto = new StoreCategorySearchRequestDto(category_name, use_yn);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StoreCategory> storeCategoryPage = storeCategoryService.getStoreCategories(storeCategorySearchRequestDto, pageable);
        // PageInfo 설정
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "카테고리 조회 성공");
        result.setList(storeCategoryPage);  // Page 데이터를 ApiResult에 설정
        result.setPageInfo(storeCategoryPage);  // PageInfo를 설정
        return result;
    }


}
