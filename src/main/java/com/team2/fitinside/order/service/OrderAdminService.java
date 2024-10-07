package com.team2.fitinside.order.service;

import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.exception.OrderNotFoundException;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // 전체 주문 조회
    public List<OrderResponseDto> findAllOrdersByAdmin() {
        // 전체 주문 조회 (isDeleted=false)
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderResponseDtoList(orders);
    }

    // 주문 상태 수정
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) throws Exception {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));
        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        findOrder.updateOrderStatus(status);
        Order updatedOrder = orderRepository.save(findOrder);
        return orderMapper.toOrderResponseDto(updatedOrder);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) throws Exception {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));
        findOrder.deleteOrder();
    }

}
