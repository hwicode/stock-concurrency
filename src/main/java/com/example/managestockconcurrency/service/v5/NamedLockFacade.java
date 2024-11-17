package com.example.managestockconcurrency.service.v5;

import com.example.managestockconcurrency.domain.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NamedLockFacade {

    private final StockServiceV5 stockService;
    private final LockRepository lockRepository;

    @Transactional
    public void decrease (Long productId, Long quantity) {
        try {
            Integer acquiredLock = lockRepository.getLock(String.valueOf(productId));
            if (acquiredLock != 1) {
                throw new RuntimeException("Lock 획득에 실패했습니다.");
            }
            stockService.decrease(productId, quantity);
        } finally {
            lockRepository.releaseLock(String.valueOf(productId));
        }
    }
}
