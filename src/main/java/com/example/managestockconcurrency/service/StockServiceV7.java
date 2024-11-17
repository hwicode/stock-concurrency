package com.example.managestockconcurrency.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class StockServiceV7 {

    // 동시성을 고려하지 않은 기본 방식
    private final StockService stockService;
    private final RedissonClient redissonClient;

    public void decrease(Long productId, Long quantity) throws InterruptedException {
        RLock lock = redissonClient.getLock(String.valueOf(productId));

        try {
            // 1: lock 획득 대기 시간, 2: 재시도 텀 3: 시간 단위
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            
            if (!available) {
                throw new RuntimeException("Lock 획득 실패");
            }
            stockService.decrease(productId, quantity);
        } finally {
            lock.unlock();
        }
    }
}
