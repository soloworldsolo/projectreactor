package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerTest {

    @Test
    void schedulertest() {
        Flux<Object> inside_sink = Flux.create(emitter -> {
            printThreadDetails("inside sink");
            emitter.next(1);
        }).doOnNext(i -> printThreadDetails("next" + i));

        inside_sink.doOnNext(i -> printThreadDetails("before excution")).
                subscribeOn(Schedulers.boundedElastic()).
                subscribe((i) -> printThreadDetails("in subscription" + i));
    }

    @Test
    void publishSubscriber() {
        Flux<Object> inside_sink = Flux.create(emitter -> {
            printThreadDetails("inside sink");
            for (int i = 0; i < 20; i++) {
                printThreadDetails("emmiting ");

                emitter.next(i);
            }
            emitter.complete();
        });

        inside_sink.doOnNext(i -> printThreadDetails("before excution ")).
                parallel().
                runOn(Schedulers.parallel()).
                doOnNext(i-> printThreadDetails("before subscribe ")).
                subscribe((i) -> printThreadDetails("in subscription " + i));

        try {
            Thread.sleep(3*1000);
        }catch (Exception e) {

        }

    }

    private void printThreadDetails(String inside_sink) {
        System.out.println(inside_sink + Thread.currentThread().getName());
    }
}
