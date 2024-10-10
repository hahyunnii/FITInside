package com.team2.fitinside.order.service;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.team2.fitinside.global.exception.ErrorCode.ORDER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // 전체 주문 조회
    public List<OrderResponseDto> findAllOrdersByAdmin() {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderResponseDtoList(orders);
    }

    // 주문 상태 수정
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.updateOrderStatus(status);
        return orderMapper.toOrderResponseDto(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        findOrder.deleteOrder();
    }

}
