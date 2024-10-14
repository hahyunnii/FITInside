package com.team2.fitinside.order.repository;

import com.team2.fitinside.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberId(Long memberId);

    Page<Order> findByMemberIdAndIsDeletedFalse(Long memberId, Pageable pageable);

    Page<Order> findAllByIsDeletedFalse(Pageable pageable);
}
