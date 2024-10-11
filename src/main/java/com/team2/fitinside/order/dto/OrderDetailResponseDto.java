package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDetailResponseDto { // 주문 상세 확인 DTO

    private Long orderId;
    private String orderStatus;
    private int totalPrice;
    private int deliveryFee;
    private String deliveryAddress;
    private String deliveryReceiver;
    private String deliveryPhone;
//    private List<Long> orderProductIds;
    private List<OrderProductResponseDto> orderProducts; // 프론트에서 바로 상품에 대한 정보를 쓸 수 있도록
    private LocalDateTime createdAt;

}
