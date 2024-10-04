package com.team2.fitinside.cart.entity;

import jakarta.persistence.*;

@Entity
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // 양방향 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // (단방향) 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    // User와 연관관계 설정 (연관관계 편의 메서드)
    public void setUser(User user) {
        this.user = user;
        user.getCarts.add(this);
    }
}
