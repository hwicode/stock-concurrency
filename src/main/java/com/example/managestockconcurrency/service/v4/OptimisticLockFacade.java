package com.example.managestockconcurrency.service.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptimisticLockFacade {

    private final StockServiceV4 stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                stockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

}
