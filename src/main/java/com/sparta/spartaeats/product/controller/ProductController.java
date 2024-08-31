package com.sparta.spartaeats.product.controller;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.product.domain.validationGroup.ValidProduct001;
import com.sparta.spartaeats.product.service.ProductService;
import com.sparta.spartaeats.product.dto.ProductRequestDto;
import com.sparta.spartaeats.product.dto.ProductResponseDto;
import com.sparta.spartaeats.product.dto.ProductSearchRequestDto;
import com.sparta.spartaeats.user.domain.validationGroup.ValidUser0001;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ApiResult createProduct(@RequestBody @Validated(ValidProduct001.class) ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    //상품 정보 수정
    @PatchMapping("/update")
    public ApiResult updateProduct(@RequestParam(value = "product_id") UUID productId, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productId, productRequestDto);
    }

    // 상품 삭제
    @PatchMapping("/delete")
    public ApiResult deleteProduct(@RequestParam(value = "product_id") UUID productId) {
        return productService.deleteProduct(productId);
    }

    //상품 상세 조회
    @GetMapping("/{product_id}")
    public ProductResponseDto getProductDetails(@PathVariable("product_id") UUID productId) {
        return productService.getProductDetails(productId);
    }

    //상품 전체 조회 및 검색
    @GetMapping
    public ApiResult getProducts(@RequestParam(value = "product_name", required = false) String productName,
                                 @RequestParam(value = "store_name", required = false) String storeName,
                                 @RequestParam(value = "use_yn", required = false) String useYn,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(value = "sort", defaultValue = "productName") String sort,
                                 @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        // 상품 전체 조회 service에 보낼 파라미터 설정
        ProductSearchRequestDto productSearchRequestDto = new ProductSearchRequestDto(productName, storeName, useYn);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortOption = Sort.by(sortDirection, sort);
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
