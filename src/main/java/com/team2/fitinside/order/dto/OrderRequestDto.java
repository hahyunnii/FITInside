package com.team2.fitinside.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto { // 주문 생성, 수정 DTO

    @NotBlank(message = "배송지를 입력해 주세요.")
    private String postalCode;

    @NotBlank(message = "배송지를 입력해 주세요.")
    private String deliveryAddress;

    private String detailedAddress;

    @NotBlank(message = "수령인을 입력해 주세요.")
    private String deliveryReceiver;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String deliveryPhone;

    private List<OrderCartRequestDto> orderItems;
    private int deliveryFee;

}
