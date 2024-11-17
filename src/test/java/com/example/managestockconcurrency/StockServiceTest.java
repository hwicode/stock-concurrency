package com.example.managestockconcurrency;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.domain.StockRepository;
import com.example.managestockconcurrency.service.StockService;
import com.example.managestockconcurrency.service.StockServiceV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockServiceV2 stockServiceV2;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void before() {
        stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
    }

    @Test
    void 재고_감소() {
        stockService.decrease(1L, 1L);
        Stock stock = stockRepository.getByProductId(1L);

        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockServiceV2.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isZero();
    }
}
