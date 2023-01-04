package com.learnreactivespring.flaxandmonopalayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void fluxUsingIterable () {
        var namesFlux = Flux.fromIterable(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray () {
        var names = new String[] {"Adam", "Anna", "Jack", "Jenny"};

        var namesFlux = Flux.fromArray(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream () {
        var namesFlux = Flux.fromStream(names.stream()).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        var mono = Mono.justOrEmpty(null).log(); // Mono.Empty()

        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "Adam";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier).log();

        System.out.println(stringSupplier.get());

        StepVerifier.create(stringMono)
                .expectNext("Adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1, 5).log();

        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
