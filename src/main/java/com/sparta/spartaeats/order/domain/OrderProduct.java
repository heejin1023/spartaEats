package com.sparta.spartaeats.order.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.order.dto.OrderProductDto;
import com.sparta.spartaeats.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_order_products")
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString
public class OrderProduct extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_product_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer price;
    private Integer amount;
    private Character delYn;

    public static OrderProduct createOrderProduct(Product product, OrderProductDto dto) {
        return OrderProduct.builder()
                .price(product.getPrice())
                .amount(dto.getAmount())
                .product(product)
                .delYn('N')
                .build();
    }

    public static List<OrderProductDto> toOrderProductDto(List<OrderProduct> orderProducts) {
        ArrayList<OrderProductDto> list = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDto orderProductDto = new OrderProductDto();
            orderProductDto.setProductId(orderProduct.getProduct().getId());
            orderProductDto.setPrice(orderProduct.getPrice());
            orderProductDto.setAmount(orderProduct.getAmount());
            list.add(orderProductDto);
        }
        return list;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
