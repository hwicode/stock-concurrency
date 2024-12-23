package com.example.managestockconcurrency.repository;

import com.example.managestockconcurrency.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock getByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock getByProductIdWithPessimisticLock(@Param("productId") Long productId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock getByProductIdWithOptimisticLock(@Param("productId") Long productId);
}
