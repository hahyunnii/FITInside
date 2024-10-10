package com.team2.fitinside.order.mapper;

import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Order -> OrderDetailResponseDto 변환 (주문 생성 후 반환 시 사용)
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target="orderProducts") // 복합 객체나 리스트는 매핑 시 명시적 선언(자동 변환)
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    OrderDetailResponseDto toOrderDetailResponseDto(Order order);

    @Mapping(source = "id", target="productId")
    OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct);

    // Order -> OrderResponseDto 변환 (주문 상태 변경 시 사용)
    @Mapping(source = "id", target = "orderId")

    OrderResponseDto toOrderResponseDto(Order order);

    // 관리자용 간단한 정보 조회 시 사용
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);

    // 리스트 타입의 변환 요청이 들어오면 각 요소에 대해 개별 매핑 메서드를 자동으로 호출
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    List<OrderUserResponseDto> toOrderUserResponseDtoList(List<Order> orders);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target = "productNames", qualifiedByName = "mapProductNames")
    OrderUserResponseDto toOrderUserResponseDto(Order order);

    @Named("mapProductNames")
    default List<String> mapProductNames(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getProductName())
                .collect(Collectors.toList());
    }
}
