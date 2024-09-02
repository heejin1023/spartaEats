package com.sparta.spartaeats.product.controller;

import com.sparta.spartaeats.common.aop.ApiLogging;
import com.sparta.spartaeats.common.controller.CustomApiController;
import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct001;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct002;
import com.sparta.spartaeats.product.service.ProductService;
import com.sparta.spartaeats.product.dto.ProductRequestDto;
import com.sparta.spartaeats.product.dto.ProductResponseDto;
import com.sparta.spartaeats.product.dto.ProductSearchRequestDto;
import com.sparta.spartaeats.store.service.StoreService;
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
@RequestMapping("/api/products")
public class ProductController extends CustomApiController {

    private final ProductService productService;
    private final StoreService storeService;

    //상품 등록
    @PostMapping
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    public ApiResult createProduct(@RequestBody @Validated(ValidProduct001.class) ProductRequestDto productRequestDto) {
        // 로그인 정보 가져오기
        User user = getLoginedUserObject();
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResult result = new ApiResult();

        //AUTH가 OWNER면 로그인 정보로 storeId 세팅
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRoleEnum.Authority.OWNER)) && user != null) {
            productRequestDto.setStoreId(storeService.getIdByOwner(user));
        }
        if (productRequestDto.getStoreId() == null){
            result.set(ApiResultError.PRODUCT_NO_STORE_ERROR);
        }

        return productService.createProduct(productRequestDto);
    }

    //상품 정보 수정
    @PatchMapping("/update")
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    public ApiResult updateProduct(@RequestParam(value = "product_id") UUID productId, @RequestBody @Validated(ValidProduct002.class) ProductRequestDto productRequestDto) {
        return productService.updateProduct(productId, productRequestDto);
    }

    // 상품 삭제
    @PatchMapping("/delete")
    @ApiLogging
    @Secured({UserRoleEnum.Authority.OWNER, UserRoleEnum.Authority.ADMIN})
    public ApiResult deleteProduct(@RequestParam(value = "product_id") UUID productId) {
        // 로그인 idx 가져오기
        Long loginUserIdx = getLoginedUserObject().getId();
        return productService.deleteProduct(loginUserIdx, productId);
    }

    //상품 상세 조회
    @GetMapping("/{product_id}")
    @ApiLogging
    public ApiResult getProductDetails(@PathVariable("product_id") UUID productId) {
        ProductResponseDto productResponseDto = productService.getProductDetails(productId);
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "음식점 상세 조회 성공").setResultData(productResponseDto);
        return result;
    }

    //상품 전체 조회 및 검색
    @GetMapping
    @ApiLogging
    public ApiResult getProducts(@RequestParam(value = "product_name", required = false) String productName,
                                 @RequestParam(value = "store_name", required = false) String storeName,
                                 @RequestParam(value = "use_yn", required = false) Character useYn,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(value = "sort", defaultValue = "productName") String sort,
                                 @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        // 상품 전체 조회 service에 보낼 파라미터 설정
        ProductSearchRequestDto productSearchRequestDto = new ProductSearchRequestDto(productName, storeName, useYn);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortOption = Sort.by(sortDirection, sort);
        // size가 10, 20, 30이 아닌 경우 10으로 조정
        if (size != 10 && size != 20 && size != 30) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page - 1, size, sortOption);
        // 상품 전체 조회 service
        Page<ProductResponseDto> productPage = productService.getProducts(productSearchRequestDto, pageable);
        // ApiResult 반환 데이터 설정
        ApiResult result = new ApiResult();
        result.set(ApiResultError.NO_ERROR, "상품 조회 성공");
        result.setList(productPage);  // Page 데이터를 ApiResult에 설정
        result.setPageInfo(productPage);  // PageInfo를 설정
        return result;
    }
}
