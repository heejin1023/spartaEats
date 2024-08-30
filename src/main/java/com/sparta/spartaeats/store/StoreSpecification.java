package com.sparta.spartaeats.store;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecification {
    public static Specification<Store> searchWith(StoreSearchRequestDto searchRequestDto) {
        return (root, query, criteriaBuilder) -> {
            // 기본 조건: 삭제되지 않은(delYn = 'N') 레코드만 검색
            Predicate predicate = criteriaBuilder.equal(root.get("delYn"), "N");

            // 검색 조건 1: 상점 이름(storeName)이 null이 아닌 경우, 해당 이름을 포함하는 레코드 추가
            if (searchRequestDto.getStoreName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("storeName"), "%" + searchRequestDto.getStoreName() + "%"));
            }

            // 검색 조건 2: 위치 ID(locationId)가 null이 아닌 경우, 해당 위치 ID와 일치하는 레코드 추가
            if (searchRequestDto.getLocationId() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("locationId"), searchRequestDto.getLocationId()));
            }

            // 검색 조건 3: 상점 주소(storeAddress)가 null이 아닌 경우, 해당 주소를 포함하는 레코드 추가
            if (searchRequestDto.getStoreAddress() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("storeAddress"), "%" + searchRequestDto.getStoreAddress() + "%"));
            }

            // 검색 조건 4: 카테고리 ID(categoryId)가 null이 아닌 경우, 해당 카테고리와 일치하는 레코드 추가
            if (searchRequestDto.getCategoryId() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("storeCategory").get("categoryId"), searchRequestDto.getCategoryId()));
            }

            // 검색 조건 5: 사용 여부(useYn)가 null이 아닌 경우, 해당 상태와 일치하는 레코드 추가
            if (searchRequestDto.getUseYn() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("useYn"), searchRequestDto.getUseYn()));
            }

            return predicate; // 최종적으로 생성된 조건 반환
        };
    }
}
