package com.sparta.spartaeats.address.repository;

import com.sparta.spartaeats.address.domain.Address;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;


@Repository
public class AddressRepositoryCustomImpl implements AddressRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Address> findByUserIdAndLocalAndOrderIdAndUseYn(Long userIdx, String local, Long orderId, Character useYn, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Address> query = cb.createQuery(Address.class);
        Root<Address> address = query.from(Address.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(address.get("user").get("id"), userIdx)); // 사용자 필터

        // 조건들
        if (local != null && !local.isEmpty()) {
            predicates.add(cb.equal(address.get("local"), local));
        }

        if (orderId != null) {
            predicates.add(cb.equal(address.get("order").get("id"), orderId));
        }

        if (useYn != null) {
            predicates.add(cb.equal(address.get("useYn"), useYn));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(address.get("createdAt"))); // 예시: 생성일순으로 정렬

        List<Address> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(result, pageable, result.size());
    }
}
