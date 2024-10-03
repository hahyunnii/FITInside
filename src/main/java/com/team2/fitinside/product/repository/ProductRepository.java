package com.team2.fitinside.product.repository;

import com.team2.fitinside.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIdAndIsDeletedFalse(Integer categoryId);

    List<Product> findByIsDeletedFalse();
}