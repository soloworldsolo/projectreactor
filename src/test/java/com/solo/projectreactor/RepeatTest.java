package com.solo.projectreactor;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class RepeatTest {
    AtomicInteger atomicInteger = new AtomicInteger(1);

    @Test
    void repeat() {

        getvalues().
                repeat(3).subscribe(i -> System.out.println("consuming items" + i));
    }

    @Test
    void repeatWithCondition() {
        getvalues().
                repeat(() -> atomicInteger.get() < 10).subscribe(i -> System.out.println("consuming items" + i));
    }

    @Test
    void retry() {
        getValuesRetry().
                retry(3).subscribe(i -> System.out.println("consuming items" + i));
    }

    @Test
    void retryWhen() {
        getValuesRetry().
                retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(3)))
                .subscribe(i -> System.out.println("consuming items" + i));


        sleep(20);

    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private Flux<Integer> getvalues() {
        return Flux.range(0, 3).
                doOnComplete(() -> System.out.println("----completed----")).
                map(i -> atomicInteger.incrementAndGet());
    }

    private Flux<Integer> getRetryWhen() {
        return Flux.range(0, 3).
                doOnComplete(() -> System.out.println("----completed----")).
                map(i -> atomicInteger.incrementAndGet());
    }


    private Flux<Integer> getValuesRetry() {
        return Flux.range(0, 3).
                map(i -> i / (Faker.instance().random().nextInt(0, 7) > 3 ? 0 : 1)).
                doOnError(err -> System.out.println(err));
    }
}
