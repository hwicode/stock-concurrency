package com.example.managestockconcurrency.service.v5;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockServiceV5 {

    private final StockRepository stockRepository;

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.getByProductIdWithOptimisticLock(productId);
        stock.decrease(quantity);
    }
}
