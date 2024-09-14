package br.com.gdarlan.projectreactorexamples.operadoresdecriacao;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class CreatorOperatorTest {

    @Test
    void testFileReadingCreate() {
        String filePath = "/home/gdarlan/Documentos/cursos/project-reactor-examples/project-reactor-examples/src/test/resources/example.txt";
        String filePath2 = "/home/gdarlan/Documentos/cursos/project-reactor-examples/project-reactor-examples/src/test/resources/example2.txt";
        Flux fileFlux = Flux.create(emitter -> {

            CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> readFileFluxSink(filePath, emitter));
            CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> readFileFluxSink(filePath2, emitter));

            CompletableFuture.allOf(task1, task2).join();

            emitter.complete();
        }).log();

        StepVerifier.create(fileFlux)
                .expectNextCount(6)
//                .expectNext("Line 1")
//                .expectNext("Line 2")
//                .expectNext("Line 3")
//                .expectNext("Line 10")
//                .expectNext("Line 20")
//                .expectNext("Line 30")
                .expectComplete()
                .verify();

    }

    private static void readFileFluxSink(String filePath, FluxSink<Object> emitter) {
        System.out.println("filePath " + filePath + " Thread " + Thread.currentThread().getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                emitter.next(line);
            }
        } catch (IOException exception) {
            emitter.error(exception);
        }
    }
}
