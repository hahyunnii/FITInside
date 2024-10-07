package com.team2.fitinside.order.service;

import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.member.entity.Authority;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    // 전체 주문 조회
    public List<OrderResponseDto> findAllOrdersByAdmin() throws AccessDeniedException {
        checkAdmin();
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderResponseDtoList(orders);
    }

    // 주문 상태 수정
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) throws AccessDeniedException {
        checkAdmin();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.updateOrderStatus(status);
        return orderMapper.toOrderResponseDto(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) throws AccessDeniedException {
        checkAdmin();
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));
        findOrder.deleteOrder();
    }

    private void checkAdmin() throws AccessDeniedException {
        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(loginMemberId).orElseThrow(() -> new IllegalStateException("인증된 사용자의 정보가 유효하지 않습니다."));

        if (findMember.getAuthority() != Authority.ROLE_ADMIN) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }

}
