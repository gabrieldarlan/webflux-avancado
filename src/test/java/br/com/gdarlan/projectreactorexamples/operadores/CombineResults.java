package br.com.gdarlan.projectreactorexamples.operadores;

import br.com.gdarlan.projectreactorexamples.introducao.YoutubeChannel;
import br.com.gdarlan.projectreactorexamples.mock.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class CombineResults {

    @Test
    void concatVideoNames() throws InterruptedException {
        Flux<String> videoNames = new YoutubeChannel(MockVideo.generateVideos())
                .getAllVideosName().delayElements(Duration.ofSeconds(1));
        Flux<String> videoNames2 = new YoutubeChannel(MockVideo.generateVideos2()).getAllVideosName();

//        videoNames.concatWith(videoNames2).log().subscribe();
        Flux.concat(videoNames, videoNames2).log().subscribe();
        Thread.sleep(10000);
    }

    @Test
    void mergeVideosName() throws InterruptedException {
        Flux<String> videoNames = new YoutubeChannel(MockVideo.generateVideos())
                .getAllVideosName().delayElements(Duration.ofMillis(500));

        Flux<String> videoNames2 = new YoutubeChannel(MockVideo.generateVideos2())
                .getAllVideosName().delayElements(Duration.ofMillis(300));

//        videoNames.mergeWith(videoNames2).log().subscribe();

        Flux.merge(videoNames, videoNames2).log().subscribe();

        Thread.sleep(10000);
    }

    @Test
    void zipWithMoney() throws InterruptedException {
        Flux<String> videoNames = new YoutubeChannel(MockVideo.generateVideos())
                .getAllVideosName().delayElements(Duration.ofMillis(100)).log();

        Flux<Double> monetization = Flux.just(100.0, 500.0, 400.0, 600.0)
                .delayElements(Duration.ofSeconds(1)).log();

//        videoNames.zipWith(monetization)
//                .map(tuple -> tuple.getT1() + ", $ " + tuple.getT2())
//                .log()
//                .subscribe();

        Flux.zip(videoNames, monetization)
                .map(tuple -> tuple.getT1() + ", $ " + tuple.getT2())
                .log()
                .subscribe();

        Thread.sleep(10000);

    }
}
