package com.example.managestockconcurrency.service;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockServiceV3 {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.getByProductIdWithPessimisticLock(productId);
        stock.decrease(quantity);
    }
}
