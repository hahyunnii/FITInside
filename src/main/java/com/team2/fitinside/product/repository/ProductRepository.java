package com.team2.fitinside.product.repository;

import com.team2.fitinside.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {


}
