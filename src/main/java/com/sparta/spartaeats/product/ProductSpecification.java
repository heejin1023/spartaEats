package com.sparta.spartaeats.product;

import com.sparta.spartaeats.product.dto.ProductSearchRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> searchWith(ProductSearchRequestDto productSearchRequestDto) {
        return (root, query, criteriaBuilder) -> {
            // 기본 조건: 삭제되지 않은(delYn = 'N') 레코드만 검색
            Predicate predicate = criteriaBuilder.equal(root.get("delYn"), "N");

            // 검색 조건 1: 상품 이름(productName)이 null이 아닌 경우, 해당 이름을 포함하는 레코드 추가
            if (productSearchRequestDto.getProductName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("productName"), "%" + productSearchRequestDto.getProductName() + "%"));
            }
            // 검색 조건 2: 상점 이름(storeName)이 null이 아닌 경우, 해당 이름을 포함하는 레코드 추가
            if (productSearchRequestDto.getStoreName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("storeName"), "%" + productSearchRequestDto.getStoreName() + "%"));
            }

            // 검색 조건 3: 사용 여부(useYn)가 null이 아닌 경우, 해당 상태와 일치하는 레코드 추가
            if (productSearchRequestDto.getUseYn() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("useYn"), productSearchRequestDto.getUseYn()));
            }

            return predicate; // 최종적으로 생성된 조건 반환
        };
    }
}
