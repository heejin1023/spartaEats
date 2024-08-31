package com.sparta.spartaeats.product.service;

import com.sparta.spartaeats.common.model.ApiResult;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.product.ProductRepository;
import com.sparta.spartaeats.product.ProductSpecification;
import com.sparta.spartaeats.product.dto.ProductRequestDto;
import com.sparta.spartaeats.product.dto.ProductResponseDto;
import com.sparta.spartaeats.product.dto.ProductSearchRequestDto;
import com.sparta.spartaeats.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    public ApiResult createProduct(ProductRequestDto productRequestDto) {
        Store store = new Store();
        store.setId(productRequestDto.getStoreId());
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .store(store)
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .productDescription(productRequestDto.getProductDescription())
                .delYn("N")
                .useYn("Y")
                .build();
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 등록 성공");
        return apiResult;
    }

    // 상품 정보 수정
    public ApiResult updateProduct(UUID productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        if (productRequestDto.getProductName() != null) {
            product.setProductName(productRequestDto.getProductName());
        }
        if (productRequestDto.getPrice() != null) {
            product.setPrice(productRequestDto.getPrice());
        }
        if (productRequestDto.getProductDescription() != null) {
            product.setProductDescription(productRequestDto.getProductDescription());
        }
        if (productRequestDto.getUseYn() != null) {
            product.setUseYn(productRequestDto.getUseYn());
        }
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 정보 수정 성공");
        return apiResult;
    }

    // 상품 삭제
    public ApiResult deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점을 찾을 수 없습니다."));
        product.setDelYn("Y");
        productRepository.save(product);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "상품 삭제 성공");
        return apiResult;
    }

    //상품 상세 조회
    public ProductResponseDto getProductDetails(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .id(productId)
                .store_id(product.getStore().getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .productDescription(product.getProductDescription())
                .use_yn(product.getUseYn())
                .build();
        return productResponseDto;
    }

    //상품 전체 조회
    public Page<ProductResponseDto> getProducts(ProductSearchRequestDto productSearchRequestDto, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(ProductSpecification.searchWith(productSearchRequestDto), pageable);
        Page<ProductResponseDto> productResponsePage = productPage.map(product -> new ProductResponseDto(
                product.getId(),
                product.getStore().getId(),
                product.getProductName(),
                product.getPrice(),
                product.getProductDescription(),
                product.getUseYn()
        ));
        return productResponsePage;
    }
}
