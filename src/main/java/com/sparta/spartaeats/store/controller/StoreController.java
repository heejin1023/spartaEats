package com.sparta.spartaeats.store.controller;

import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.store.domain.validationGroup.ValidStore001;
import com.sparta.spartaeats.store.service.StoreService;
import com.sparta.spartaeats.store.dto.StoreRequestDto;
import com.sparta.spartaeats.store.dto.StoreResponseDto;
import com.sparta.spartaeats.store.dto.StoreSearchRequestDto;
import com.sparta.spartaeats.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController extends CustomApiController {
    private final StoreService storeService;

    // 음식점 등록
    @PostMapping
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    public ApiResult createStore(@RequestBody @Validated(ValidStore001.class) StoreRequestDto storeRequestDto) {
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ApiResult result = new ApiResult();

        // 권한이 OWNER인 경우, 로그인한 사용자 정보 세팅
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRoleEnum.Authority.OWNER))) {
            User user = getLoginedUserObject();
            if (user != null) {
                storeRequestDto.setUserIdx(user.getId());
            }
        }
        if (storeRequestDto.getUserIdx() == null){
            result.set(ApiResultError.STORE_NO_OWNER);
        }

        //authority가 admin이라면 storeRequestDto 그대로 넘기기
        return storeService.createStore(storeRequestDto);
    }

    // 음식점 정보 수정
    @PatchMapping("/update")
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    public ApiResult updateStore(@RequestParam(value = "store_id") UUID id, @RequestBody StoreRequestDto storeRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //권한이 owner일 경우 현재 로그인한 사용자의 store_id인지 확인
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRoleEnum.Authority.OWNER))) {
            boolean isOwnerOfStore = storeService.isOwnerOfStore(getLoginedUserObject().getId(), id);
            if (!isOwnerOfStore) {
                // 권한이 없거나 다른 사용자의 음식점일 경우
                ApiResult result = new ApiResult();
                return result.set(ApiResultError.NO_AUTH_ERROR);
            }
        }
        return storeService.updateStore(id, storeRequestDto);
    }

    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    @PatchMapping("/delete")
    public ApiResult deleteStore(@RequestParam(value = "store_id") UUID id) {
        return storeService.deleteStore(id);

    }
    // 음식점 상세 조회
    @GetMapping("/{store_id}")
    public StoreResponseDto getStoreDetail(@PathVariable("store_id") UUID id) {
//        ApiResult apiResult = new ApiResult();
//        apiResult.set(ApiResultError.NO_ERROR, "음식점 상세 조회 성공").setResultData(responseDto);
//        return apiResult;
        return storeService.getStoreDetail(id);
    }

    // 음식점 전체 조회 및 검색
    @GetMapping
    public ApiResult getStores(@RequestParam(value = "store_name", required = false) String storeName,
                               @RequestParam(value = "location_id", required = false) String locationId,
                               @RequestParam(value = "store_address", required = false) String storeAddress,
                               @RequestParam(value = "category_id", required = false) String categoryId,
                               @RequestParam(value = "use_yn", required = false) String useYn,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
                               @RequestParam(value = "direction", defaultValue = "desc") String direction) {

        StoreSearchRequestDto searchRequestDto = new StoreSearchRequestDto(storeName, locationId, storeAddress, categoryId, useYn);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortOption = Sort.by(sortDirection, sort);

        Pageable pageable = PageRequest.of(page - 1, size, sortOption);
        Page<StoreResponseDto> storeResponseDtoPage = storeService.getStores(searchRequestDto, pageable);
        // PageInfo 설정
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "음식점 조회 성공");
        result.setList(storeResponseDtoPage);  // Page 데이터를 ApiResult에 설정
        result.setPageInfo(storeResponseDtoPage);  // PageInfo를 설정
        return result;
    }
}
