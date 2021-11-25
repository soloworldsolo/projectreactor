package com.solo.projectreactor;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

class FluxClassTest {
    FluxClass fluxClass;

    @BeforeEach
    void setUp() {
        fluxClass = new FluxClass();
    }

    @Test
    void test1() {
        fluxClass.getFluxJust().subscribe(res -> System.out.println(res));
    }


    @Test
    void fluxSinkTest() {
        Flux.create(fluxSink -> {
            String name = Faker.instance().country().name();
            while (!name.equalsIgnoreCase("india")) {
                fluxSink.next(name);
                name = Faker.instance().country().name();
            }
            fluxSink.complete();
        }).subscribe(res -> System.out.println(res));
    }

    @Test
    void fluxSinkTest2() {
        FluxSinkClass fluxSinkClass = new FluxSinkClass();

        Flux.create(fluxSinkClass).subscribe(res -> System.out.println(res));
        fluxSinkClass.produce();
    }

    /**
     * runs infinitely
     */
    @Test
    void fluxGenerateTest() {
        Flux.generate(synchronousSink -> {
            synchronousSink.next(Faker.instance().beer().name());
        }).take(15).subscribe(res -> System.out.println(res));
    }


    @Test
    void fluxGenerateSinkCounter() {
        Flux.generate(() -> 1,
                (counter, synchronousSink) -> {
                    if (counter > 10) {
                        synchronousSink.complete();
                    }
                    synchronousSink.next(Faker.instance().country().name());
                    return ++counter;
                }).subscribe(res -> System.out.println(res));
    }

    @Test
    void fluxHandlertest() {
        Flux.range(0, 10).handle((s, sink) -> {
            sink.next(s);
        }).subscribe(s -> System.out.println(s));
    }

    /**
     * requesting for n items
     * first it will send n- numbner of items in limit then give new request
     */
    @Test
    void limitRate() {
        Flux.range(0, 1000).
                log().limitRate(100).
                subscribe(s -> System.out.println(s));
    }

    @Test
    void errorHandling() {
        //
        Flux.range(0,10).
                map(i-> i/ (5-i)).
                onErrorReturn(Integer.MIN_VALUE).subscribe(i->System.out.println(i));

        //continues the flow
        Flux.range(0,10).
                map(i-> i/(5-i))
                .onErrorContinue((e , res)-> System.out.println(Integer.MIN_VALUE)).
                subscribe(i-> System.out.println(i));
        //sstops when exception is thrown
        Flux.range(0,10).
                map(i-> i/(5-i))
                .onErrorResume((e)-> Mono.just(Integer.MAX_VALUE)).
                subscribe(i-> System.out.println(i));

    }


    @Test
    void transformHandlers() {
        Flux<String> nameflux = Flux.range(0, 15).map(i -> Faker.instance().country().name());

        nameflux.transform(i->transformFunction(i)).subscribe(op -> System.out.println(op));
    }


    public Flux<String> transformFunction(Flux<String> inputflux) {

        return inputflux.filter(s -> !s.startsWith("S")).map(s-> s.toUpperCase());

    }
}