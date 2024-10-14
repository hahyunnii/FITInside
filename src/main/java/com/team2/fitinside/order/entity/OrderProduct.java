package com.team2.fitinside.order.entity;

import com.team2.fitinside.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_product_name", nullable = false)
    private String orderProductName;

    @Column(name = "order_product_price", nullable = false)
    private int orderProductPrice;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // 주문 상품 삭제
    public void deleteOrderProduct(){
        this.isDeleted = true;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
