package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto { // 주문 확인 DTO (관리자 목록용)

    private Long orderId;
    private String orderStatus;
    private int totalPrice;
    private String deliveryAddress;
    private LocalDateTime createdAt;

}
