package com.sparta.spartaeats.store_category;

import com.sparta.spartaeats.store.Store;
import com.sparta.spartaeats.store.StoreSearchRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StoreCategorySpecification {
    public static Specification<StoreCategory> searchWith(StoreCategorySearchRequestDto storeCategorySearchRequestDto) {
        return (root, query, criteriaBuilder) -> {
            // 기본 조건: 삭제되지 않은(delYn = 'N') 레코드만 검색
            Predicate predicate = criteriaBuilder.equal(root.get("delYn"), "N");

            // 검색 조건 1: 상점 이름(storeName)이 null이 아닌 경우, 해당 이름을 포함하는 레코드 추가
            if (storeCategorySearchRequestDto.getCategoryName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("storeName"), "%" + storeCategorySearchRequestDto.getCategoryName() + "%"));
            }

            // 검색 조건 5: 사용 여부(useYn)가 null이 아닌 경우, 해당 상태와 일치하는 레코드 추가
            if (storeCategorySearchRequestDto.getUseYn() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("useYn"), storeCategorySearchRequestDto.getUseYn()));
            }

            return predicate; // 최종적으로 생성된 조건 반환
        };
    }
}
