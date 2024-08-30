package com.sparta.spartaeats.product;

import com.sparta.spartaeats.common.model.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ApiResult createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    //상품 정보 수정
    @PatchMapping("/{product_id}/update")
    public ApiResult updateProduct(@PathVariable UUID product_id, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(product_id, productRequestDto);
    }

    // 상품 삭제
    @PatchMapping("/{product_id}/delete")
    public ApiResult deleteProduct(@PathVariable UUID product_id) {
        return productService.deleteProduct(product_id);
    }

    //상품 상세 조회

    //상품 전체 조회 및 검색

}
