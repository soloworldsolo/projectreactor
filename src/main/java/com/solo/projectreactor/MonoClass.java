package com.solo.projectreactor;

import com.github.javafaker.Faker;
import reactor.core.publisher.Mono;

public class MonoClass {
    public static String getName() {
        return Faker.instance().beer().name();
    }

    public Mono<String> getMonoOf() {
        return Mono.just("welcome");
    }

    public Mono<String> getMonoFromSupplier() {
        return Mono.fromSupplier(MonoClass::getName);
    }
}
