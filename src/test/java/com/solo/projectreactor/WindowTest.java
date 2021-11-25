package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

public class WindowTest {

    private Flux<String> getFluxDetails() {
        return   Flux.interval(Duration.ofMillis(300)).
                map(i->"event"+ i);
    }
    @Test
    public  void windowTest() {
        getFluxDetails().window(4).
                flatMap(e->processElements(e)).
                subscribe(i-> System.out.println("acknowlegement after completing batch"));

        sleep(30);
    }


    @Test
    public  void windowInterTest() {
        getFluxDetails().window(Duration.ofSeconds(3)).
                flatMap(e->processElements(e)).
                subscribe(i-> System.out.println("acknowlegement after completing batch"));

        sleep(30);
    }

    @Test
    void fluxGroupByTest() {
        Flux.range(0,30)
                .delayElements(Duration.ofMillis(300)).
                groupBy(i -> (i&1)==0)
                .subscribe(gflux-> processFlux(gflux ,gflux.key()));

        sleep(15);
    }

    private void processFlux(Flux<Integer> gflux, Boolean key) {
        gflux.subscribe(value -> System.out.println("Key is"+ key + "value is"+ value));

    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Mono<Integer> processElements(Flux<String> lists) {
       return lists.doOnNext(s -> System.out.println("saving "+s)).
                doOnComplete(() -> {
                 System.out.println("successfully completed the batch process");
                 System.out.println("-------------------------------------------");
                }).then(Mono.just(new Random().nextInt()));
    }
}
