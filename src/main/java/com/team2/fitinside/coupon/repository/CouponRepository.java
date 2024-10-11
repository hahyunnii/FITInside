package com.team2.fitinside.coupon.repository;

import com.team2.fitinside.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 활성화된 쿠폰들 조회
    Page<Coupon> findByActiveIs(Pageable pageable, boolean isActive);

    // 만료일이 지난 쿠폰 조회
    List<Coupon> findByExpiredAtLessThanEqual(LocalDate now);

    Optional<Coupon> findByCode(String code);

    List<Coupon> findByNameContains(String name);
}
