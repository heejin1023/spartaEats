package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.common.model.ApiResult;
import lombok.RequiredArgsConstructor;
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
    @PatchMapping("/{store_category_id}/update")
    public ApiResult updateStoreCategory(@RequestParam UUID store_category_id, @RequestBody StoreCategoryRequestDto storeCategoryRequestDto) {
        return storeCategoryService.updateStoreCategory(store_category_id, storeCategoryRequestDto);
    }

    //카테고리 삭제
    @PatchMapping("/{store_category_id}/update/delete")
    public ApiResult deleteStoreCategory(@RequestParam UUID store_category_id) {
        return storeCategoryService.deleteStoreCategory(store_category_id);
    }

    //카테고리 상세 조회

    //카테고리 전체 조회 및 검색



}
