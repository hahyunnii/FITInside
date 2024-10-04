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

    // OrderRequestDto -> Order 변환 (주문 생성 시 사용)
    @Mapping(target = "createdAt", ignore = true)
    Order toOrder(OrderRequestDto orderRequestDto);

    // OrderProduct -> OrderProductResponseDto 변환 (주문 생성 후 반환 시 사용)
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "orderProductPrice", target = "productPrice")
    OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct);


    // Order -> OrderDetailResponseDto 변환 (주문 생성 후 반환 시 사용)
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target="orderProducts") // 복합 객체나 리스트는 매핑 시 명시적 선언 필요
    OrderDetailResponseDto toOrderDetailResponseDto(Order order);

    // Order -> OrderResponseDto 변환 (주문 상태 변경 시 사용)
    @Mapping(source = "id", target = "orderId")
    OrderResponseDto toOrderResponseDto(Order order);

    // 관리자용 간단한 정보 조회 시 사용
    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);

    //////////// 회원 주문 목록 확인 시 상품 목록은 간단히 상품 이름 리스트로 반환 ///////////////
    // 리스트 타입의 변환 요청이 들어오면 각 요소에 대해 개별 매핑 메서드를 자동으로 호출
    List<OrderUserResponseDto> toOrderUserResponseDtoList(List<Order> orders);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target = "productNames", qualifiedByName = "mapProductNames")
    OrderUserResponseDto toOrderUserResponseDto(Order order);

    // 상품 이름 리스트 생성 메서드
    @Named("mapProductNames")
    default List<String> mapProductNames(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getProductName())
                .collect(Collectors.toList());
    }
    /////////////////////////////////////////////////////////////////////////////////
}
