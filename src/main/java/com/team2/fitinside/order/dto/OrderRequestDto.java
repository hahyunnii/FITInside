package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto { // 주문 생성, 수정 DTO

    private String deliveryAddress;
    private String deliveryReceiver;
    private String deliveryPhone;
    private List<OrderCartRequestDto> orderItems;
    private int deliveryFee;

}
