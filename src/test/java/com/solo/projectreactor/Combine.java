package com.solo.projectreactor;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Combine {
    List<String> bufferList;

    @Test
    void test1() {
        Flux<String> source = getNames();

        source.take(5).subscribe(sourc -> System.out.println(sourc));
        sleep(6);
        System.out.println(bufferList.size());
    }

    private Flux<String> getNames() {
        bufferList = new ArrayList<>();
        return Flux.generate(emmiter -> {
            String name = Faker.instance().name().firstName();
            emmiter.next(name);
            bufferList.add(name);
        }).cast(String.class).startWith(getNamesFromCache());

    }

    @Test
    void fluxConcatnate() {
        Flux<String> flux1 = Flux.just("1", "2", "3", "4");
        Flux<String> flux2 = Flux.just("5", "6", "7", "8");
        Flux.concat(flux1, flux2).subscribe(res -> System.out.print(res));
    }

    @Test
    void fluxMerge() {
        Flux<String> flux1 = Flux.just("1", "2", "3", "4");
        Flux<String> flux2 = Flux.just("5", "6", "7", "8");
        Flux.merge(flux1, flux2).subscribe(res -> System.out.print(res));
    }

    @Test
    void combineLatest() {
        Flux<String> flux1 = Flux.just("A", "B", "C", "D", "E").delayElements(Duration.ofSeconds(1));
        Flux<Integer> flux2 = Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofSeconds(3));
        Flux.combineLatest(flux1, flux2, (a, b) -> a + b).subscribe(res -> System.out.println(res));
        sleep(10);
    }

    private Flux<String> getNamesFromCache() {
        System.out.println("reading from cache");
        return Flux.fromStream(bufferList.stream());
    }

    @Test
    void testBuffer() {
      getFluxDetails().buffer(25).
              subscribe(out -> System.out.println(out));

      sleep(30);
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
