package com.team2.fitinside.order.service;

import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.coupon.entity.CouponMember;
import com.team2.fitinside.coupon.repository.CouponMemberRepository;
import com.team2.fitinside.coupon.service.CouponService;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import com.team2.fitinside.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team2.fitinside.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CouponService couponService;
    private final CouponMemberRepository couponMemberRepository;
    private final SecurityUtil securityUtil;

    // 주문 조회 (회원)
    public OrderDetailResponseDto findOrder(Long orderId) {

        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        checkAuthorization(findOrder);

        return orderMapper.toOrderDetailResponseDto(findOrder);
    }

    // 전체 주문 조회 (회원)
    public OrderUserResponseWrapperDto findAllOrders(int page, String productName) {

        Long loginMemberId = securityUtil.getCurrentMemberId();

        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("createdAt").descending());
        Page<Order> ordersPage = orderRepository.findByMemberIdAndProductName(loginMemberId, productName, pageable);

        List<OrderUserResponseDto> orders = orderMapper.toOrderUserResponseDtoList(ordersPage.getContent());

        return new OrderUserResponseWrapperDto(orders, ordersPage.getTotalPages());

    }

    // 주문 생성
    @Transactional
    public OrderDetailResponseDto createOrder(OrderRequestDto request) {

        Long loginMemberId = securityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(USER_NOT_AUTHORIZED));

        List<Cart> carts = cartRepository.findAllByMember_Id(loginMemberId);
        if (carts.isEmpty()) {
            throw new CustomException(CART_EMPTY);
        }

        // 주문 생성
        Order order = Order.builder()
                .member(findMember)
                .deliveryFee(request.getDeliveryFee())
                .postalCode(request.getPostalCode())
                .deliveryAddress(request.getDeliveryAddress())
                .detailedAddress(request.getDetailedAddress())
                .deliveryReceiver(request.getDeliveryReceiver())
                .deliveryPhone(request.getDeliveryPhone())
                .deliveryMemo(request.getDeliveryMemo())
                .build();

        // 장바구니의 각 상품 -> OrderProduct 변환 후 주문에 추가
        for (Cart cart : carts) {
            Product product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                throw new CustomException(OUT_OF_STOCK);
            }

            product.sold(cart.getQuantity()); // 재고 차감

            // 장바구니 상품ID와 일치하는 상품ID를 갖고 있는 OrderCartRequestDto 조회
            OrderCartRequestDto orderCartRequestDto = request.getOrderItems().stream()
                    .filter(dto -> dto.getProductId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ORDER_PRODUCT_NOT_FOUND));

            // id에 맞는 couponMember 조회
            CouponMember couponMember = null;
            if (orderCartRequestDto.getCouponMemberId() != null) {
                couponMember = couponMemberRepository.findById(orderCartRequestDto.getCouponMemberId())
                        .orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));
            }

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .orderProductName(product.getProductName())
                    .orderProductPrice(product.getPrice())
                    .count(cart.getQuantity())
                    .couponMember(couponMember)
                    .discountedPrice(orderCartRequestDto.getDiscountedTotalPrice())
                    .build();

            // 쿠폰 사용
            if (orderCartRequestDto.getCouponMemberId() != null) {
                couponService.redeemCoupon(orderCartRequestDto.getCouponMemberId());
            }

            // 주문에 상품 추가 (총가격도 업데이트)
            order.addOrderProduct(orderProduct);
            cartRepository.delete(cart);
        }

        // 주문(+주문상품) 저장
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderDetailResponseDto(createdOrder);
    }

    // 주문 수정
    @Transactional
    public OrderDetailResponseDto updateOrder(Long orderId, OrderRequestDto request) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        checkAuthorization(order);

        if (order.getOrderStatus() != OrderStatus.ORDERED) {
            throw new CustomException(ORDER_MODIFICATION_NOT_ALLOWED);
        }

        order.updateDeliveryInfo(request);
        return orderMapper.toOrderDetailResponseDto(order);

    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {

        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        checkAuthorization(findOrder);

        if (findOrder.getOrderStatus() != OrderStatus.ORDERED) {
            throw new CustomException(ORDER_MODIFICATION_NOT_ALLOWED);
        }

        findOrder.cancelOrder();
    }

    private void checkAuthorization(Order order) {
        Long loginMemberId = securityUtil.getCurrentMemberId();
        if (!loginMemberId.equals(order.getMember().getId())) {
            throw new CustomException(USER_NOT_AUTHORIZED);
        }
    }

}
