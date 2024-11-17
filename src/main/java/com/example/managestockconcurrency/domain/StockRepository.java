package com.example.managestockconcurrency.domain;

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
}
