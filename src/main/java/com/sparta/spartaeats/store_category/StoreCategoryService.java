package com.sparta.spartaeats.store_category;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class StoreCategoryService {
    private final StoreCategoryRepository storeCategoryRepository;
//    private final JPAQueryFactory queryFactory;

    //카테고리 등록

    public ApiResult createStoreCategory(StoreCategoryRequestDto storeCategoryRequestDto){
        System.out.println(storeCategoryRequestDto.getCategory_name());
        System.out.println(storeCategoryRequestDto.getCategory_description());
        StoreCategory storeCategory = StoreCategory.builder()
                .id(UUID.randomUUID())
                .categoryName(storeCategoryRequestDto.getCategory_name())
                .categoryDescription(storeCategoryRequestDto.getCategory_description())
                .build();
        storeCategoryRepository.save(storeCategory);
        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 등록 성공");
        return apiResult;
    }

    //카테고리 정보 수정
    public ApiResult updateStoreCategory(UUID store_category_id, StoreCategoryRequestDto storeCategoryRequestDto){
        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(store_category_id, "N")
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        storeCategory.setCategoryName(storeCategoryRequestDto.getCategory_name());
        storeCategory.setCategoryDescription(storeCategory.getCategoryDescription());
        storeCategory.setUseYn(storeCategory.getUseYn());

//        storeCategoryRepository.save(storeCategory);

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 정보 수정 성공");
        return apiResult;
    }

    //카테고리 삭제
    public ApiResult deleteStoreCategory(UUID store_category_id) {
        StoreCategory storeCategory = storeCategoryRepository.findByIdAndDelYn(store_category_id, "N")
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        storeCategory.setDelYn("Y");

        ApiResult apiResult = new ApiResult();
        apiResult.set(ApiResultError.NO_ERROR, "카테고리 삭제 성공");
        return apiResult;

    }




    // 카테고리 상세 조회
    @Transactional(readOnly = true)
    public ApiResult getCategoryById(UUID storeCategoryId) {
        StoreCategory storeCategory = storeCategoryRepository.findById(storeCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return new ApiResult()
                .setResultData(storeCategory)
                .setResultMessage("카테고리 상세 조회 성공");
    }

//    // 카테고리 목록 조회 및 검색
//    @Transactional(readOnly = true)
//    public ApiResult getCategories(String categoryName, String useYn, Pageable pageable) {
//        QStoreCategory qStoreCategory = QStoreCategory.storeCategory;
//
//        // 동적 쿼리 작성
//        List<StoreCategory> storeCategories = queryFactory
//                .selectFrom(qStoreCategory)
//                .where(
//                        categoryNameEq(categoryName),
//                        useYnEq(useYn),
//                        delYnEq("N")  // 기본 조건: 삭제되지 않은 항목만 조회
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // 페이지 계산을 위한 전체 카운트 쿼리
//        long total = queryFactory
//                .selectFrom(qStoreCategory)
//                .where(
//                        categoryNameEq(categoryName),
//                        useYnEq(useYn),
//                        delYnEq("N")
//                )
//                .fetchCount();
//
//        // Page 객체 생성
//        Page<StoreCategory> storeCategoryPage = PageableExecutionUtils.getPage(storeCategories, pageable, () -> total);
//
//        return new ApiResult()
//                .setList(storeCategoryPage)
//                .setResultMessage("카테고리 목록 조회 성공");
//    }
//
//    // 조건 메서드
//    private BooleanExpression categoryNameEq(String categoryName) {
//        return categoryName != null && !categoryName.isEmpty() ? QStoreCategory.storeCategory.categoryName.containsIgnoreCase(categoryName) : null;
//    }
//
//    private BooleanExpression useYnEq(String useYn) {
//        return useYn != null && !useYn.isEmpty() ? QStoreCategory.storeCategory.useYn.eq(useYn) : null;
//    }
//
//    private BooleanExpression delYnEq(String delYn) {
//        return delYn != null && !delYn.isEmpty() ? QStoreCategory.storeCategory.delYn.eq(delYn) : null;
//    }
}
