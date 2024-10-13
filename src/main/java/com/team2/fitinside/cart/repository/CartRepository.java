package com.team2.fitinside.cart.repository;

import com.team2.fitinside.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByMember_Id(Long memberId);

    boolean existsCartByMember_IdAndProduct_Id(Long memberId, Long productId);

    Optional<Cart> findByMember_IdAndProduct_Id(Long memberId, Long productId);

    // 장바구니 조회 시 상품 정보 포함
    @Query("SELECT p.productName, p.price, c.quantity " +
            "FROM Cart c " +
            "Join c.product p " +
            "WHERE c.member.id = :memberId")
    List<Object[]> findCartProductsByMemberId(@Param("memberId") Long MemberId);

}
