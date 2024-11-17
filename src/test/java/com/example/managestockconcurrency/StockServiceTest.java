package com.example.managestockconcurrency;

import com.example.managestockconcurrency.domain.Stock;
import com.example.managestockconcurrency.repository.StockRepository;
import com.example.managestockconcurrency.service.*;
import com.example.managestockconcurrency.service.v4.OptimisticLockFacade;
import com.example.managestockconcurrency.service.v5.NamedLockFacade;
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
    private StockServiceV3 stockServiceV3;

    @Autowired
    private OptimisticLockFacade optimisticLockFacade;

    @Autowired
    private NamedLockFacade namedLockFacade;

    @Autowired
    private StockServiceV6 stockServiceV6;

    @Autowired
    private StockServiceV7 stockServiceV7;

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
    void 동시에_100명의_유저가_재고_감소_요청_실패_V1() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isNotZero();
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V2() throws InterruptedException {
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

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V3() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockServiceV3.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V4() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockFacade.decrease(1L, 1L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V5() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    namedLockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V6() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockServiceV6.decrease(1L, 1L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.getByProductId(1L);
        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    void 동시에_100명의_유저가_재고_감소_요청_V7() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockServiceV7.decrease(1L, 1L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
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
