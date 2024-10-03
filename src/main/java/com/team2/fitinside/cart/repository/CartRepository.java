package com.team2.fitinside.cart.repository;

import com.team2.fitinside.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUser_Email(String email);

    boolean existsCartByUser_IdAndProduct_Id(Long userId, Long productId);
}
