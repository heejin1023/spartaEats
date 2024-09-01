package com.sparta.spartaeats.ai.domain;

import com.sparta.spartaeats.ai.dto.AiArchiveSearchCondition;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class AiArchiveSpecification {
    public static Specification<AiArchive> searchWith(AiArchiveSearchCondition searchRequestDto) {
        return (root, query, criteriaBuilder) -> {
            // 기본 조건: 삭제되지 않은(delYn = 'N') 레코드만 검색
            Predicate predicate = criteriaBuilder.equal(root.get("delYn"), "N");



            return predicate; // 최종적으로 생성된 조건 반환
        };
    }
}
