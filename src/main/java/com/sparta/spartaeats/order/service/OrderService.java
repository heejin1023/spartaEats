package com.sparta.spartaeats.order.service;

import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.order.domain.OrderProduct;
import com.sparta.spartaeats.responseDto.MultiResponseDto;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.responseDto.SingleResponseDto;
import com.sparta.spartaeats.entity.*;
import com.sparta.spartaeats.exception.DeletedProductException;
import com.sparta.spartaeats.exception.EmptyDataException;
import com.sparta.spartaeats.order.dto.*;
import com.sparta.spartaeats.delivery.DeliveryRepository;
import com.sparta.spartaeats.order.repository.OrderProductRepository;
import com.sparta.spartaeats.order.repository.OrderRepository;
import com.sparta.spartaeats.product.repository.ProductRepository;
import com.sparta.spartaeats.store.repository.StoreRepository;
import com.sparta.spartaeats.types.OrderStatus;
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
    private final DeliveryRepository deliveryRepository;
    private final OrderProductRepository orderProductRepository;


    public SingleResponseDto order(OrderRequestDto orderRequestDto){ // , User user

            Store findStore = storeRepository.findByIdWithDel(orderRequestDto.getStoreId()).orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다"));
            Delivery delivery = deliveryRepository.findByIdWithDel(orderRequestDto.getDeliveryId()).orElseThrow(() -> new IllegalArgumentException("해당 배달 주소를 찾을 수 없습니다"));
            List<OrderProductDto> orderProducts = orderRequestDto.getOrderProducts();
            //OrderProductDto 로 OrderProduct List 생성
            ArrayList<OrderProduct> orderProductsList = getOrderProductsList(orderProducts);
            if (orderProductsList.isEmpty()) {
                log.error("Order.order orderProductsList is empty");
                throw new EmptyDataException("주문 상품이 존재 하지 않습니다");
            }
            for (OrderProduct orderProduct : orderProductsList) {
                if (orderProduct.getDelYn() == 'Y') {
                    log.error("Order.order orderProduct is Invalid");
                    throw new DeletedProductException("판매 중지 된 상품이 존재합니다, 상품명 : " + orderProduct.getProduct().getProductName());
                }
            }
        User user = userRepository.findById(252).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        Order order = new Order(user, findStore, orderProductsList,
                    orderRequestDto.getMemo(), orderRequestDto.getOrderType(), Order.calculatePrice(orderProductsList), delivery, OrderStatus.PREPARING, 'N');
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
            if (findProduct.getDelYn() == 'Y') {
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
        findOrder.changeOrderStatus(orderId,requestDto.getOrderStatus());
        return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 상태 변경 완료", new UpdateOrderResponseDto(orderId, requestDto.getOrderStatus()));
    }

    @Transactional(readOnly = true)
    public SingleResponseDto getOneOrder(UUID orderId, String userRole) {
            log.info("userRole = {}", userRole);
            Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
    //        Order findOrder = orderRepository.findByIdWithAuth(orderId, user.getId()).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
            OrderListResponseDto responseDto = new OrderListResponseDto(orderId, findOrder.getUser().getId(), findOrder.getStore().getId(),
                    Order.calculatePrice(findOrder.getOrderProductList()), findOrder.getMemo(),
                    findOrder.getOrderStatus(), findOrder.getOrderType(),
                    OrderProduct.toOrderProductDto(findOrder.getOrderProductList()));
            return new SingleResponseDto<>(ApiResultError.NO_ERROR, "주문 내역 단일 조회", responseDto);
    }

    @Transactional(readOnly = true)
    public MultiResponseDto getOrderList(OrderSearchCondition cond, Pageable pageable) {
            return orderRepository.searchOrders(cond, pageable);
        }

    public SimpleResponseDto cancelOrder(UUID orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
            return order.cancelOrder();
    }

    public SimpleResponseDto deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with" + orderId));
        return order.deleteOrder();
    }
}
