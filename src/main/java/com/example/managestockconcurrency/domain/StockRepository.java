package com.example.managestockconcurrency.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock getByProductId(Long productId);
}
