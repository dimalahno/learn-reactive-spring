package com.learnreactivespring.flaxandmonopalayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        var stringFlux = Flux.just("A", "B", "C", "D", "F", "E")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(s -> System.out.println("Subscriber 1 : " + s));

        Thread.sleep(3000);

        stringFlux.subscribe(s -> System.out.println("Subscriber 2 : " + s));

        Thread.sleep(4000);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        var stringFlux = Flux.just("A", "B", "C", "D", "F", "E")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();

        connectableFlux.connect();
        connectableFlux.subscribe(s -> System.out.println("Subscriber 1 : " + s));
        Thread.sleep(3000);

        connectableFlux.subscribe(s -> System.out.println("Subscriber 2 : " + s)); //does not emit the values from beginning

        Thread.sleep(4000);
    }
}
