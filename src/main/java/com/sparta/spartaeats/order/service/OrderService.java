package com.sparta.spartaeats.order.service;

import com.sparta.spartaeats.address.domain.Address;
import com.sparta.spartaeats.address.repository.AddressRepository;
import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.common.exception.DeletedProductException;
import com.sparta.spartaeats.common.exception.EmptyDataException;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.order.domain.OrderProduct;
import com.sparta.spartaeats.order.dto.*;
import com.sparta.spartaeats.order.repository.OrderProductRepository;
import com.sparta.spartaeats.order.repository.OrderRepository;

import com.sparta.spartaeats.product.domain.Product;
import com.sparta.spartaeats.product.repository.ProductRepository;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;

import com.sparta.spartaeats.store.domain.Store;
import com.sparta.spartaeats.store.repository.StoreRepository;
import com.sparta.spartaeats.common.type.OrderStatus;
import com.sparta.spartaeats.user.domain.User;
import com.sparta.spartaeats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository deliveryRepository;
    private final OrderProductRepository orderProductRepository;


    public SingleResponseDto order(OrderRequestDto orderRequestDto,User user){ // , User user

            Store findStore = storeRepository.findByIdWithDel(orderRequestDto.getStoreId()).orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다"));
            Address delivery = deliveryRepository.findByIdWithDel(orderRequestDto.getDeliveryId()).orElseThrow(() -> new IllegalArgumentException("해당 배달 주소를 찾을 수 없습니다"));
            List<OrderProductDto> orderProducts = orderRequestDto.getOrderProducts();
            //OrderProductDto 로 OrderProduct List 생성
            ArrayList<OrderProduct> orderProductsList = getOrderProductsList(orderProducts);
            if (orderProductsList.isEmpty()) {
                try {
                    log.error("Order.order orderProductsList is empty");
                    throw new EmptyDataException("주문 상품이 존재 하지 않습니다");
                } catch (EmptyDataException e) {
                    return new SingleResponseDto(ApiResultError.ERROR_EMPTY_DATA, e.getMessage(), null);
                }

            }
            for (OrderProduct orderProduct : orderProductsList) {
                if (orderProduct.getDelYn() == 'Y') {
                    log.error("Order.order orderProduct is Invalid");
                    throw new DeletedProductException("판매 중지 된 상품이 존재합니다, 상품명 : " + orderProduct.getProduct().getProductName());
                }
            }
        Order order = new Order(user, findStore, orderProductsList,
                    orderRequestDto.getMemo(), orderRequestDto.getOrderType(), Order.calculatePrice(orderProductsList), delivery, OrderStatus.PENDING, 'N');
            // 연관관계 설정
            // 루프 내에서 Iterable 직접 수정 시 발생하는 에러로 인해 복제
            ArrayList<OrderProduct> list = new ArrayList<>(orderProductsList);
            for (OrderProduct orderProduct : list) {
                order.addOrderProduct(orderProduct);
                orderProductRepository.save(orderProduct);
            }
            orderRepository.save(order);
            OrderResponseDto orderResponseDto = Order.toResponseDto(order);
            return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 성공", orderResponseDto);

    }

    private ArrayList<OrderProduct> getOrderProductsList(List<OrderProductDto> orderProducts) {
        ArrayList<OrderProduct> list = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderProducts) {
            Product findProduct = productRepository.findByIdWithDel(orderProductDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
            if (findProduct.getDelYn() == "Y") {
                log.error("Order.getOrderProductList findProduct is Invalid");
                throw new DeletedProductException("판매 중지 된 상품이 존재합니다, 상품명 : " + findProduct.getProductName());
            }
            OrderProduct orderProduct = OrderProduct.createOrderProduct(findProduct, orderProductDto);
            list.add(orderProduct);
        }
        return list;
    }

    public SingleResponseDto updateOrder(UUID orderId, UpdateOrderDto requestDto) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
        try {
            findOrder.changeOrderStatus(orderId, requestDto.getOrderStatus());
        } catch (IllegalStateException e) {
            return new SingleResponseDto(ApiResultError.ERROR_INVALID_STATE, e.getMessage(), null);
        }
        return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 상태 변경 완료", new UpdateOrderResponseDto(orderId, requestDto.getOrderStatus()));
    }

    @Transactional(readOnly = true)
    public SingleResponseDto getOneOrder(UUID orderId, User user) {
        log.info("userRole = {}", user.getUserId());
        String userRole = String.valueOf(user.getUserRole());
        Long userId = user.getId();
        // 권한이 USER 일땐 자기가 주문한 내역만
        if (userRole.contains("USER")) {
            try {
                Order findOrder = orderRepository.findByIdAndUser(orderId, user).orElseThrow(() -> new EmptyDataException("Order not found with" + orderId));
                OrderListResponseDto responseDto = getResponseDto(orderId, findOrder);
                return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 내역 단일 조회", responseDto);
            } catch (EmptyDataException e) {
                return new SingleResponseDto(ApiResultError.ERROR_EMPTY_DATA, e.getMessage(), null);
            }
            // 권한이 OWNER 이면 자기 가게의 주문 중에서
        }else if(userRole.contains("OWNER")) {
            try {
                Store store = storeRepository.findByOwner(user).orElseThrow(() -> new EmptyDataException("Store not found with owner"));
                Order findOrder = orderRepository.findByIdAndStore(orderId, store).orElseThrow(() -> new EmptyDataException("Order not found with" + orderId));
                OrderListResponseDto responseDto = getResponseDto(orderId, findOrder);
                return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 내역 단일 조회", responseDto);
            }
            catch (EmptyDataException e) {
                return new SingleResponseDto(ApiResultError.ERROR_EMPTY_DATA, e.getMessage(), null);
            }
            // ADMIN은 전체 중에서 검색
        }else {
            Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
            OrderListResponseDto responseDto = getResponseDto(orderId, findOrder);
            return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 내역 단일 조회", responseDto);
        }
    }

    private static OrderListResponseDto getResponseDto(UUID orderId, Order findOrder) {
        return new OrderListResponseDto(orderId, findOrder.getUser().getId(), findOrder.getStore().getId(),
                Order.calculatePrice(findOrder.getOrderProductList()), findOrder.getMemo(),
                findOrder.getOrderStatus(), findOrder.getOrderType(),
                OrderProduct.toOrderProductDto(findOrder.getOrderProductList()));
    }

    @Transactional(readOnly = true)
    public MultiResponseDto getOrderList(OrderSearchCondition cond, Pageable pageable,User user) {
        log.info("userRole = {}", user.getUserId());
        try {
            String userRole = String.valueOf(user.getUserRole());
            Long userId = user.getId();
            if(userRole.contains("USER")) {
                return orderRepository.searchOrdersWithUserRole(cond, pageable, userId);
            } else if (userRole.contains("OWNER")) {
                Store findStore = storeRepository.findByOwner(user).orElseThrow(() -> new EmptyDataException("Store not found with owner"));
                return orderRepository.searchOrdersWithOwnerRole(cond,pageable,findStore);
            } else
                return orderRepository.searchOrders(cond, pageable);
        } catch (EmptyDataException e) {
            return new MultiResponseDto(ApiResultError.ERROR_EMPTY_DATA, e.getMessage());
        }
    }

    public SimpleResponseDto cancelOrder(UUID orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
            return order.cancelOrder();
    }

    public SimpleResponseDto deleteOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
        return order.deleteOrder(userId);
    }
}
