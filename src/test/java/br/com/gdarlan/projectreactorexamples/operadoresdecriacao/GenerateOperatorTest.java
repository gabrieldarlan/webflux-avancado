package br.com.gdarlan.projectreactorexamples.operadoresdecriacao;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GenerateOperatorTest {

    @Test
    void testFileReadingGenerate() {
        String filePath = "/home/gdarlan/Documentos/cursos/project-reactor-examples/project-reactor-examples/src/test/resources/example.txt";

        Flux fileFlux = Flux.generate(
                () -> {
                    try {
                        System.out.println("creating bufferedReader");
                        return new BufferedReader(new FileReader(filePath));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                },
                ((bufferedReader, synchronousSink) -> {
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            synchronousSink.next(line);
                        } else {
                            synchronousSink.complete();
                        }
                    } catch (IOException exception) {
                        synchronousSink.error(exception);
                    }
                    return bufferedReader;
                }),
                bufferedReader -> {
                    try {
                        System.out.println("End - closing file");
                        bufferedReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).log();

        StepVerifier.create(fileFlux)
                .expectNext("Line 1")
                .expectNext("Line 2")
                .expectNext("Line 3")
                .expectComplete()
                .verify();
    }
}
