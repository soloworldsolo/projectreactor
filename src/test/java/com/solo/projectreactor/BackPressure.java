package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class BackPressure {
    @Test
    void buffer() {
        Flux.create(fluxSink -> {
                    for (int i = 0; i < 1000; i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                }).onBackpressureBuffer().
                publishOn(Schedulers.boundedElastic()).
                doOnNext((i) -> {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).subscribe(i -> System.out.println("writing" + i));
    }

    @Test
    void drop()  {
        Flux.create(fluxSink -> {
                    for (int i = 0; i < 1000; i++) {
                        fluxSink.next(i);
                        sleep(1);
                    }
                    fluxSink.complete();
                }).onBackpressureDrop().
                publishOn(Schedulers.boundedElastic()).
                doOnNext((i) -> {

                      sleep(4);

                }).subscribe(i -> System.out.println("writing" + i));

           sleep(7*1000);
    }

    @Test
    void latest()  {
        Flux.create(fluxSink -> {
                    for (int i = 0; i < 1000 && !fluxSink.isCancelled(); i++) {
                        fluxSink.next(i);
                        sleep(1);
                    }
                    fluxSink.complete();
                }).onBackpressureError().
                publishOn(Schedulers.boundedElastic()).
                doOnNext((i) -> {

                    sleep(4);

                }).subscribe(i -> System.out.println("writing" + i));

        sleep(7*1000);
    }


    void sleep(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
