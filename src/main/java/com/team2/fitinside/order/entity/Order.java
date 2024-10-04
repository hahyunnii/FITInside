package com.team2.fitinside.order.entity;

import com.team2.fitinside.order.common.OrderStatus;
import com.team2.fitinside.order.dto.OrderProductDto;
import com.team2.fitinside.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_receiver", nullable = false)
    private String deliveryReceiver;

    @Column(name = "delivery_phone", nullable = false)
    private String deliveryPhone;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // 하나의 주문에 여러 상품이 있을 수 있음
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    // 주문 상태 변경
    public void updateOrderStatus(OrderStatus status){
        this.orderStatus = status;
    }

    // 주문 취소
    public void cancelOrder(){
        if(this.orderStatus == OrderStatus.COMPLETED){
            throw new IllegalStateException("이미 배송이 시작된 주문은 취소할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }

    // 주문 삭제
    public void deleteOrder(){
        this.isDeleted = true;
    }

    public void addOrderProduct(OrderProduct orderProduct){
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this); // OrderProduct에도 해당 Order 설정 (양방향 관계)
    }



}
