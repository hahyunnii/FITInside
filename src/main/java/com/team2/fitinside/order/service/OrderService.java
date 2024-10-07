package com.team2.fitinside.order.service;

import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.cart.service.CartService;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    // 다른 모듈의 service를 이용하는 게 관심사 분리를 적합하게 한 것 (추후 수정)
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    // 주문 조회 (회원)
    public OrderDetailResponseDto findOrder(Long userId, Long orderId) throws Exception {

        // 회원 조회
        Member findMember = memberRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getMember().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        return orderMapper.toOrderDetailResponseDto(findOrder);
    }

    // 전체 주문 조회 (회원)
    public List<OrderUserResponseDto> findAllOrders(Long userId) throws Exception {

        // 회원 조회
        memberRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 전체 주문 조회 (isDeleted=false)
        List<Order> orders = orderRepository.findByMemberId(userId).stream()
                .filter(order -> !order.isDeleted())
                .collect(Collectors.toList());

        return orderMapper.toOrderUserResponseDtoList(orders);

    }

    // 주문 생성
    @Transactional
    public OrderDetailResponseDto createOrder(Long userId, OrderRequestDto request) throws Exception {

        // 회원 조회
        Member findMember = memberRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 회원 장바구니 정보 가져오기
        List<Cart> carts = cartRepository.findAllByMember_Id(userId);
        if (carts.isEmpty()) {
            throw new CartEmptyException("장바구니가 비어 있습니다.");
        }

        // 주문 생성
        Order order = Order.builder()
                .member(findMember)
                .deliveryFee(2500)  // 하드코딩.. 나중에 배송비 관련 로직 수정
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryReceiver(request.getDeliveryReceiver())
                .deliveryPhone(request.getDeliveryPhone())
                .build();

        // 장바구니의 각 상품 -> OrderProduct 변환 후 주문에 추가
        for (Cart cart : carts) {
            // 재고 확인
            Product product = cart.getProduct(); // 상품 정보
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
            cartService.deleteCart(cart.getId()); // 로컬 스토리지도 삭제해야 함 나중에 확인!
        }

        // 주문 저장
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderDetailResponseDto(createdOrder);
    }

    // 주문 수정
    @Transactional
    public OrderDetailResponseDto updateOrder(Long userId, Long orderId, OrderRequestDto request) throws Exception {

        // 회원 조회
        Member findMember = memberRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getMember().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        // 배송 전이면 주문 수정
        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new OrderModificationNotAllowedException("배송이 시작된 주문은 수정할 수 없습니다.");
        }
        findOrder.updateDeliveryInfo(request);

        // 주문 저장
        Order updatedOrder = orderRepository.save(findOrder);
        return orderMapper.toOrderDetailResponseDto(updatedOrder);

    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long userId, Long orderId) throws Exception {

        // 회원 조회
        Member findUser = memberRepository.findById(userId).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        // 권한 확인 -> 추후 수정
        if (!findOrder.getMember().getId().equals(userId)) {
            throw new Exception("해당 주문에 대한 권한이 없습니다.");
        }

        // 배송 전이면 주문 취소
        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new OrderModificationNotAllowedException("배송이 시작된 주문은 취소할 수 없습니다.");
        }
        findOrder.cancelOrder();

    }

}
