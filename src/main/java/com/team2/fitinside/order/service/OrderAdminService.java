package com.team2.fitinside.order.service;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderResponseWrapperDto;
import com.team2.fitinside.order.dto.OrderStatusResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public OrderResponseWrapperDto findAllOrdersByAdmin(int page) {
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by("createdAt").descending());
        Page<Order> ordersPage = orderRepository.findAllByIsDeletedFalse(pageable);

        List<OrderResponseDto> orders = orderMapper.toOrderResponseDtoList(ordersPage.getContent());

        return new OrderResponseWrapperDto(orders, ordersPage.getTotalPages());
    }

    // 주문 상태 수정
    @Transactional
    public OrderStatusResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.updateOrderStatus(status);
        return orderMapper.toOrderStatusResponseDto(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        findOrder.deleteOrder();
    }

}
