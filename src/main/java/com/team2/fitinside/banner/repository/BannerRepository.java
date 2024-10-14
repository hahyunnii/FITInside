package com.team2.fitinside.banner.repository;

import com.team2.fitinside.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByIsDeletedFalse();
}
