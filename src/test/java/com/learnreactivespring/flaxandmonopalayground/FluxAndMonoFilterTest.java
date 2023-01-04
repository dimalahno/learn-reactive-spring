package com.learnreactivespring.flaxandmonopalayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void filterTest() {
        Flux<String> fluxNames = Flux.fromIterable(names)
                .filter(name -> name.startsWith("A"))
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("Adam", "Anna")
                .verifyComplete();
    }

    @Test
    public void filterTestLength() {
        Flux<String> fluxNames = Flux.fromIterable(names)
                .filter(name -> name.length() > 4)
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("Jenny")
                .verifyComplete();
    }
}
