package com.solo.projectreactor;

import reactor.core.publisher.Flux;

public class FluxClass {
    public Flux<String> getFluxJust() {
        return Flux.just("a", "b", "c");
    }
}
