package br.com.gdarlan.projectreactorexamples.operadoresdecriacao;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class SimpleCreatorOperatorsTest {

    @Test
    void fluxJust() {
        Flux<Integer> simpleFlux = Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofMillis(1000))
                .log();

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void fluxFromIterable() {
        Flux<Integer> simpleFlux = Flux.fromIterable(List.of(1, 2, 3, 4, 5)).log();
        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    void fluxFromArray() {
        Flux<Integer> simpleFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4}).log();

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void fluxFromStream() {
        Flux<Integer> simpleFlux = Flux.fromStream(Stream.of(1, 2, 3, 4)).log();
        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void fluxRange() {
        Flux<Integer> simpleFlux = Flux.range(1, 4).log();
        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void monoJust() {
        Mono<Integer> monoJust = Mono.just(1).log();
        StepVerifier.create(monoJust)
                .expectNext(1)
                .verifyComplete();

    }
}
