package com.team2.fitinside.order.repository;

import com.team2.fitinside.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberId(Long memberId);

    Page<Order> findByMemberIdAndIsDeletedFalse(Long memberId, Pageable pageable);

    Page<Order> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "LEFT JOIN FETCH op.couponMember cm " +
            "LEFT JOIN FETCH cm.coupon c " +
            "WHERE o.isDeleted = false")
    List<Order> findAllOrdersWithDetails(Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN o.orderProducts op " +
            "JOIN op.product p " +
            "WHERE o.member.id = :memberId " +
            "AND o.isDeleted = false " +
            "AND p.productName LIKE %:productName%")
    Page<Order> findByMemberIdAndProductName(@Param("memberId") Long memberId,
                                             @Param("productName") String productName,
                                             Pageable pageable);

}
