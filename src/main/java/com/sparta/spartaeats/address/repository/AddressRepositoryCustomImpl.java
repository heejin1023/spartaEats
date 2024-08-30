package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.core.Address;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public class AddressRepositoryCustomImpl implements AddressRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Address> findByUserIdAndConditions(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Address> query = cb.createQuery(Address.class);
        Root<Address> address = query.from(Address.class);

        Predicate criteria = cb.conjunction();

        // 사용자 필터링
        criteria = cb.and(criteria, cb.equal(address.get("user").get("id"), userIdx));

        // 선택적인 조건들
        if (local != null && !local.isEmpty()) {
            criteria = cb.and(criteria, cb.equal(address.get("local"), local));
        }

        if (orderId != null) {
            criteria = cb.and(criteria, cb.equal(address.get("order").get("id"), orderId));
        }

        if (useYn != null) {
            criteria = cb.and(criteria, cb.equal(address.get("useYn"), useYn));
        }

        query.where(criteria);

        // 정렬 예시: 생성일순으로 정렬
        query.orderBy(cb.desc(address.get("createdAt")));

        TypedQuery<Address> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList();
    }
}
