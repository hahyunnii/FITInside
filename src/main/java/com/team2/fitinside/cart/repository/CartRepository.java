package com.team2.fitinside.cart.repository;

import com.team2.fitinside.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByMember_Email(String email);

    boolean existsCartByMember_EmailAndProduct_Id(String email, Long productId);

    Optional<Cart> findByMember_EmailAndProduct_Id(String email, Long productId);
}
