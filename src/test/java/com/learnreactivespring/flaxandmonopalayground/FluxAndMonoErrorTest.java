package com.learnreactivespring.flaxandmonopalayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Flux.just("default", "default1");
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
//                .expectError(RuntimeException.class)
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default")
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap () {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap_withRetry () {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retry(2)
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }
}
