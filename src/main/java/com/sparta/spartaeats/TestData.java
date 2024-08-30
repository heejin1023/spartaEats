//package com.sparta.spartaeats;
//
//import com.sparta.spartaeats.common.type.UserRoleEnum;
//import com.sparta.spartaeats.entity.*;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//public class TestData {
//
//    private final TestDataService testDataService;
//
//    @PostConstruct
//    public void init() {
//        testDataService.init();
//    }
//
//    @Component
//    static class TestDataService {
//
//        @PersistenceContext
//        private EntityManager em;
//
//        @Transactional
//        public void init() {
//            User userA = new User("userId", "1234", "userA", "010-1111-1111", "test@test.com", UserRoleEnum.USER, null,
//                    null, 'n', 'y', LocalDateTime.now());
//            User userB = new User("userId2", "1234", "userB", "010-2222-2222", "test@test.com", UserRoleEnum.USER, null,
//                    null, 'n', 'y', LocalDateTime.now());
//            User userC = new User("userId2", "1234", "userC", "010-2222-2222", "test@test.com", UserRoleEnum.USER, null,
//                    null, 'n', 'y', LocalDateTime.now());
//            em.persist(userA);
//            em.persist(userB);
//            em.persist(userC);
//            Category category = new Category("한식", "한국의맛", 'y', 'n');
//            Category category2 = new Category("양식", "양놈이맛", 'y', 'n');
//            Category category3 = new Category("중식", "짜장", 'y', 'n');
//            Store store = new Store(userA, category, null, null, null, "한식맛집", "01011111111", "종로구", 'y', 'n');
//            Store store2 = new Store(userB, category2, null, null, null, "양식맛집", "01022222222", "종로구", 'y', 'n');
//            Store store3 = new Store(userC, category2, null, null, null, "중식맛집", "01033333333", "종로구", 'y', 'n');
//            Product product = new Product(store, "된장", 7000, "된장찌개정식", 'y', 'n');
//            Product product2 = new Product(store, "불백", 8000, "불백", 'y', 'n');
//            Product product3 = new Product(store2, "스테이크", 20000, "스테이크", 'y', 'n');
//            Product product4 = new Product(store3, "짜장", 6500, "간짜장면", 'y', 'n');
//            em.persist(category);
//            em.persist(category2);
//            em.persist(category3);
//            em.persist(store);
//            em.persist(store2);
//            em.persist(store3);
//            em.persist(product);
//            em.persist(product2);
//            em.persist(product3);
//            em.persist(product4);
//            Delivery delivery = new Delivery(userA, null, "321", "광화문", "서울", "뭐", "010123321312", 'n', 'y');
//            Delivery delivery2 = new Delivery(userB, null, "321", "우리집", "서울", "뭐", "010123321312", 'n', 'y');
//            em.persist(delivery);
//            em.persist(delivery2);
//
//
//        }
//    }
//}