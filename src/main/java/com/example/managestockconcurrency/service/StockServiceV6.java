package com.example.managestockconcurrency.service;

import com.example.managestockconcurrency.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockServiceV6 {

    // 동시성을 고려하지 않은 기본 방식
    private final StockService stockService;
    private final RedisLockRepository redisLockRepository;

    public void decrease(Long productId, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(productId)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(productId, quantity);
        } finally {
            redisLockRepository.unLock(productId);
        }
    }
}
