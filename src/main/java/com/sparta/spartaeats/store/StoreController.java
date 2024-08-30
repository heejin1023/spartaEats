package com.sparta.spartaeats.store;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    // 음식점 등록
    @PostMapping
    public ApiResult createStore(@RequestBody StoreRequestDto storeRequestDto) {
        return storeService.createStore(storeRequestDto);
    }

    // 음식점 정보 수정
    @PatchMapping("/update")
    public ApiResult updateStore(@RequestParam UUID storeId, @RequestBody StoreRequestDto storeRequestDto) {
        return storeService.updateStore(storeId, storeRequestDto);
    }

    @PatchMapping("/delete")
    public ApiResult deleteStore(@RequestParam UUID storeId) {
        return storeService.deleteStore(storeId);

    }
    // 음식점 상세 조회
    @GetMapping("/{store_id}")
    public StoreResponseDto getStoreDetail(@PathVariable("store_id") UUID storeId) {
//        ApiResult apiResult = new ApiResult();
//        apiResult.set(ApiResultError.NO_ERROR, "음식점 상세 조회 성공").setResultData(responseDto);
//        return apiResult;
        return storeService.getStoreDetail(storeId);
    }

    // 음식점 전체 조회 및 검색
    @GetMapping
    public ApiResult getStores(@RequestParam(value = "store_name", required = false) String storeName,
                               @RequestParam(value = "location_id", required = false) String locationId,
                               @RequestParam(value = "store_address", required = false) String storeAddress,
                               @RequestParam(value = "category_id", required = false) String categoryId,
                               @RequestParam(value = "use_yn", required = false) String useYn,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {

        StoreSearchRequestDto searchRequestDto = new StoreSearchRequestDto(storeName, locationId, storeAddress, categoryId, useYn);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StoreResponseDto> storeResponseDtoPage = storeService.getStores(searchRequestDto, pageable);
        // PageInfo 설정
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "음식점 조회 성공");
        result.setList(storeResponseDtoPage);  // Page 데이터를 ApiResult에 설정
        result.setPageInfo(storeResponseDtoPage);  // PageInfo를 설정
        return result;
    }
}
