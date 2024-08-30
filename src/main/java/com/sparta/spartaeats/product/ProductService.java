package com.sparta.spartaeats.product;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    public ApiResult createProduct(ProductRequestDto productRequestDto) {
        Store store = new Store();
        store.setId(productRequestDto.getStore_id());
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .store(store)
                .productName(productRequestDto.getProduct_name())
                .price(productRequestDto.getPrice())
                .productDescription(productRequestDto.getProduct_description())
                .del_yn("N")
                .use_yn("Y")
                .build();
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 등록 성공");
        return apiResult;
    }

    public ApiResult updateProduct(UUID productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        product.setProductName(product.getProductName());
        product.setPrice(productRequestDto.getPrice());
        product.setProductDescription(productRequestDto.getProduct_description());
        product.setUse_yn(productRequestDto.getUse_yn());
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 정보 수정 성공");
        return apiResult;
    }

    public ApiResult deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        product.setDel_yn("Y");
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 삭제 성공");
        return apiResult;
    }
}
