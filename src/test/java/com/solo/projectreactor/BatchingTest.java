package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class BatchingTest {

    @Test
    void testBuffer() {
        getFluxDetails().buffer(25).
                subscribe(out -> System.out.println(out));

        sleep(30);
    }
    @Test
    void testBufferByTime() {
        getFluxDetails().buffer(Duration.ofSeconds(2)).
                subscribe(out -> System.out.println(out));

        sleep(15);
    }

    @Test
    void testBufferByTimeOut() {
        getFluxDetails().bufferTimeout(16,Duration.ofSeconds(2)).
                subscribe(out -> System.out.println(out));

        sleep(15);
    }



    private Flux<String> getFluxDetails() {
        return   Flux.interval(Duration.ofMillis(300)).
                map(i->"event"+ i);
    }

    void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
