package com.example.managestockconcurrency.service.v4;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockServiceV4 {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.getByProductIdWithOptimisticLock(productId);
        stock.decrease(quantity);
    }
}
