package com.example.managestockconcurrency.service;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockServiceV2 {

    private final StockRepository stockRepository;

    public synchronized void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.getByProductId(productId);
        stock.decrease(quantity);
        stockRepository.save(stock);
    }
}
