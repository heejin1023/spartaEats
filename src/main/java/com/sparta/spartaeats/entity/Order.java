package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.common.type.ApiResultError;
import com.sparta.spartaeats.responseDto.SimpleResponseDto;
import com.sparta.spartaeats.exception.OrderTimeOutException;
import com.sparta.spartaeats.order.dto.OrderResponseDto;
import com.sparta.spartaeats.types.OrderStatus;
import com.sparta.spartaeats.types.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList = new ArrayList<>();

    private String memo;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private Integer orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delvr_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Character delYn;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public static Integer calculatePrice(List<OrderProduct> list) {
        return list.stream()
                .mapToInt(OrderProduct::getPrice)
                .sum();
    }

    public static OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setUserId(order.getUser().getId());
        orderResponseDto.setStoreId(order.getStore().getId());
        orderResponseDto.setOrderPrice(order.getOrderPrice());
        orderResponseDto.setCreatedAt(LocalDateTime.now());
        return orderResponseDto;
    }

    public void changeOrderStatus(UUID orderId, OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Delivered Orders cannot be changed");
        }
        this.orderStatus = orderStatus;
    }

    public Order(User user, Store store, List<OrderProduct> orderProductList, String memo, OrderType orderType, Integer orderPrice, Delivery delivery, OrderStatus orderStatus, Character delYn) {
        this.user = user;
        this.store = store;
        this.orderProductList = orderProductList;
        this.memo = memo;
        this.orderType = orderType;
        this.orderPrice = orderPrice;
        this.delivery = delivery;
        this.orderStatus = orderStatus;
        this.delYn = delYn;
    }

    public SimpleResponseDto cancelOrder() {
        LocalDateTime createdAt = this.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        if (duration.toMinutes() > 5) {
//            return new SimpleResponseDto(ApiResultError.ERROR_TIMEOUT.getCode(), "주문 후 5분이 초과하여 취소할 수 없습니다");
            throw new OrderTimeOutException("주문 후 5분이 초과하여 취소할 수 없습니다");
        }
        this.orderStatus = OrderStatus.CANCELED;
        return new SimpleResponseDto(ApiResultError.NO_ERROR.getCode(), "주문이 취소되었습니다");
    }

    public SimpleResponseDto deleteOrder() {
        if (this.getDelYn() == 'Y') {
            throw new IllegalArgumentException("이미 삭제된 주문 내역입니다");
//            return new SimpleResponseDto(ApiResultError.ERROR_PARAMETERS.getCode(), "이미 삭제된 주문 내역입니다");
        }else {
            this.delYn = 'Y';
            this.deletedAt = LocalDateTime.now();
            return new SimpleResponseDto(ApiResultError.NO_ERROR.getCode(), "주문 내역이 삭제되었습니다");
        }
    }
}
