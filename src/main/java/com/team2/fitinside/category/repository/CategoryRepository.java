package com.team2.fitinside.category.repository;
import com.team2.fitinside.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Soft delete가 되지 않은 카테고리만 조회
    List<Category> findAllByIsDeletedFalse();
}
