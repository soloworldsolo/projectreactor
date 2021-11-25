package com.solo.projectreactor;

import com.github.javafaker.Faker;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class FluxSinkClass implements Consumer<FluxSink<String>> {
    private FluxSink<String> fluxSink ;
    @Override
    public void accept(FluxSink<String> stringFluxSink) {
        fluxSink = stringFluxSink;
    }

    public void produce() {
        this.fluxSink.next(Faker.instance().name().name());

    }
}
