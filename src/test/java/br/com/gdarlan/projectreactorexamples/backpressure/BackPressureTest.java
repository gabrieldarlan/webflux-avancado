package br.com.gdarlan.projectreactorexamples.backpressure;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackPressureTest {
    @Test
    void backPressureBuffer() throws InterruptedException {

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.interval(Duration.ofMillis(1))
                .onBackpressureBuffer(20)
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " Consumindo numero: " + number);
                    return number;
                })
                .subscribe();

        Thread.sleep(50_0000);
    }

    @Test
    void backPressureError() throws InterruptedException {

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.interval(Duration.ofMillis(1))
                .onBackpressureError()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " Consumindo numero: " + number);
                    return number;
                })
                .subscribe();

        Thread.sleep(50_0000);
    }

    @Test
    void backPressureDrop() throws InterruptedException {

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " Consumindo numero: " + number);
                    return number;
                })
                .subscribe();

        Thread.sleep(50_0000);
    }


    @Test
    void backPressureLatest() throws InterruptedException {

        System.setProperty("reactor.bufferSize.small", "16");

        Flux.interval(Duration.ofMillis(1))
                .onBackpressureLatest()
                .publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " Consumindo numero: " + number);
                    return number;
                })
                .subscribe();

        Thread.sleep(50_0000);
    }

    @Test
    void creatorOperatorBackPressureStrategies() throws InterruptedException {
        System.setProperty("reactor.bufferSize.small", "16");

        Flux<Object> fluxTest = Flux.create(emitter -> {
            for (int i = 0; i < 10_0000; i++) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.LATEST);

        fluxTest.publishOn(Schedulers.single())
                .map(number -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " Consumindo numero: " + number);
                    return number;
                })
                .subscribe();

        Thread.sleep(50_0000);
    }
}
