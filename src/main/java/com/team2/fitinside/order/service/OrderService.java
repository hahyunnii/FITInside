package com.team2.fitinside.order.service;

import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderRequestDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import com.team2.fitinside.order.exception.CartEmptyException;
import com.team2.fitinside.order.exception.OrderModificationNotAllowedException;
import com.team2.fitinside.order.exception.OrderNotFoundException;
import com.team2.fitinside.order.exception.OutOfStockException;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import com.team2.fitinside.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    // 주문 조회 (회원)
    public OrderDetailResponseDto findOrder(Long orderId) throws AccessDeniedException {

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        if (!findOrder.getMember().getId().equals(loginMemberId)) {
            throw new AccessDeniedException("해당 주문에 대한 권한이 없습니다.");
        }

        return orderMapper.toOrderDetailResponseDto(findOrder);
    }

    // 전체 주문 조회 (회원)
    public List<OrderUserResponseDto> findAllOrders() {

        Long loginMemberId = SecurityUtil.getCurrentMemberId();

        List<Order> orders = orderRepository.findByMemberId(loginMemberId).stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderUserResponseDtoList(orders);

    }

    // 주문 생성
    @Transactional
    public OrderDetailResponseDto createOrder(OrderRequestDto request) {

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(loginMemberId).orElseThrow(() -> new IllegalStateException("인증된 사용자 정보가 유효하지 않습니다."));

        List<Cart> carts = cartRepository.findAllByMember_Id(loginMemberId);
        if (carts.isEmpty()) {
            throw new CartEmptyException("장바구니가 비어 있습니다.");
        }

        // 주문 생성
        Order order = Order.builder()
                .member(findMember)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryReceiver(request.getDeliveryReceiver())
                .deliveryPhone(request.getDeliveryPhone())
                .build();

        // 장바구니의 각 상품 -> OrderProduct 변환 후 주문에 추가
        for (Cart cart : carts) {
            Product product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                throw new OutOfStockException("품절된 상품입니다. productId: " + product.getId());
            }

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .orderProductName(product.getProductName())
                    .orderProductPrice(product.getPrice())
                    .count(cart.getQuantity())
                    .build();

            // 주문에 상품 추가 (총가격도 업데이트)
            order.addOrderProduct(orderProduct);
            cartRepository.delete(cart); // 로컬 스토리지도 삭제해야 함 나중에 확인!
        }

        // 주문(+주문상품) 저장
        order.calculateDeliveryFee();
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderDetailResponseDto(createdOrder);
    }

    // 주문 수정
    @Transactional
    public OrderDetailResponseDto updateOrder(Long orderId, OrderRequestDto request) throws AccessDeniedException {

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        if (!order.getMember().getId().equals(loginMemberId)) {
            throw new AccessDeniedException("해당 주문에 대한 권한이 없습니다.");
        }

        if (order.getOrderStatus() != OrderStatus.ORDERED) {
            throw new OrderModificationNotAllowedException("배송이 시작된 주문은 수정할 수 없습니다.");
        }

        order.updateDeliveryInfo(request);
        return orderMapper.toOrderDetailResponseDto(order);

    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) throws AccessDeniedException {

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        if (!findOrder.getMember().getId().equals(loginMemberId)) {
            throw new AccessDeniedException("해당 주문에 대한 권한이 없습니다.");
        }

        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new OrderModificationNotAllowedException("배송이 시작된 주문은 취소할 수 없습니다.");
        }

        findOrder.cancelOrder();

    }

}
