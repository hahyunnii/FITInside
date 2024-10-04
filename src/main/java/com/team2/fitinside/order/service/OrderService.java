package com.team2.fitinside.order.service;

import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.user.entity.User;
import com.team2.fitinside.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    // 주문 생성
    @Transactional
    public OrderDetailResponseDto createOrder(Long userId, OrderRequestDto request) throws Exception {

        // 회원 조회
        User findUser = userRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 회원 장바구니 정보 가져오기
        List<Cart> carts = cartRepository.findAllByUser_Email(findUser.getEmail());
        if (carts.isEmpty()) {
            throw new Exception("장바구니가 비어 있습니다.");
        }

        // 주문 생성
        Order order = Order.builder()
                .user(findUser)
                .deliveryFee(2500)  // 하드코딩.. 나중에 배송비 관련 로직 수정
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryReceiver(request.getDeliveryReceiver())
                .deliveryPhone(request.getDeliveryPhone())
                .build();

        // 장바구니의 각 상품 -> OrderProduct 변환 후 주문에 추가
        for (Cart cart : carts) {
            Product product = cart.getProduct(); // 상품 정보
            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .orderProductPrice(product.getPrice())
                    .count(cart.getQuantity())
                    .build();

            // 주문에 상품 추가 (총가격도 업데이트)
            order.addOrderProduct(orderProduct);
        }

        // 주문 저장
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderDetailResponseDto(createdOrder);
    }

    // 주문 수정 (회원)
    @Transactional
    public OrderDetailResponseDto updateOrder(Long userId, Long orderId, OrderRequestDto request) throws Exception {

        // 회원 조회
        User findUser = userRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new Exception("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getUser().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        // 배송 전이면 주문 수정
        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new Exception("배송이 시작된 주문은 수정할 수 없습니다.");
        }
        findOrder.updateDeliveryInfo(request);

        // 주문 저장
        Order updatedOrder = orderRepository.save(findOrder);
        return orderMapper.toOrderDetailResponseDto(updatedOrder);

    }

    // 주문 상태 수정 (관리자)
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) throws Exception {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new Exception("주문이 존재하지 않습니다."));
        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        findOrder.updateOrderStatus(status);
        Order updatedOrder = orderRepository.save(findOrder);
        return orderMapper.toOrderResponseDto(updatedOrder);
    }

    // 주문 취소 (회원)
    @Transactional
    public void cancelOrder(Long userId, Long orderId) throws Exception {

        // 회원 조회
        User findUser = userRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new Exception("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getUser().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        // 배송 전이면 주문 취소
        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new Exception("배송이 시작된 주문은 취소할 수 없습니다.");
        }
        findOrder.cancelOrder();

    }

    // 주문 삭제 (관리자)
    @Transactional
    public void deleteOrder(Long orderId) throws Exception {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new Exception("주문이 존재하지 않습니다."));
        findOrder.deleteOrder();
    }

    // 주문 조회 (회원)
    public OrderDetailResponseDto findOrder(Long userId, Long orderId) throws Exception {

        // 회원 조회
        User findUser = userRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new Exception("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getUser().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        return orderMapper.toOrderDetailResponseDto(findOrder);
    }

    // 전체 주문 조회 (회원)
    public List<OrderUserResponseDto> findAllOrders(Long userId) throws Exception {

        // 회원 조회
        userRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 전체 주문 조회 (isDeleted=false)
        List<Order> orders = orderRepository.findByUserId(userId).stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderUserResponseDtoList(orders);

    }

    public List<OrderResponseDto> findAllOrdersByAdmin() {
        // 전체 주문 조회 (isDeleted=false)
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderResponseDtoList(orders);
    }
}
