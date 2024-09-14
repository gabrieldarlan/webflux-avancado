package br.com.gdarlan.projectreactorexamples.errorhandler;

import br.com.gdarlan.projectreactorexamples.introducao.*;
import br.com.gdarlan.projectreactorexamples.mock.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

public class ErrorHandlerTest {

    @Test
    void onErrorReturnMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(video -> monetizationCalculator.calculate(video))
                .onErrorReturn(0.0)
                .subscribe(value -> System.out.println("$ " + value));
    }

    @Test
    void onErrorResumeMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(video -> monetizationCalculator.calculate(video))
                .onErrorResume(exception -> {
                    System.out.println("onErrorResume");
                    return Flux.just(0.0, 999.00);
                })
                .subscribe(value -> System.out.println("$ " + value));
    }

    @Test
    void onErrorContinueMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorContinue(((throwable, object) -> {
                    Video video = (Video) object;
                    System.out.println("onErrorContinue " + video.getName());
                }))
                .subscribe(System.out::println);
    }

    @Test
    void onErrorMapMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorMap(throwable -> {
                    System.out.println("onErrorMap");
                    throw new MonetizationException("Less than 10k views");
                }).subscribe(System.out::println);
    }

    @Test
    void onErrorCompleteMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .onErrorComplete()
                .doFinally(signalType -> System.out.println("Sinal " + signalType))
                .subscribe(System.out::println);
    }

    @Test
    void isEmptyMonetization() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());
        MonetizationCalculator monetizationCalculator = new MonetizationCalculator();

        youtubeChannel
                .getAllVideos()
                .flatMap(monetizationCalculator::calculate)
                .switchIfEmpty(Flux.just(0.0, 0.1))
                .subscribe(System.out::println);
    }

    @Test
    void retryMonetization() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();

        youtubeChannel.getAllVideos()
                .log()
                .map(videoAnalyser::analyse)
//                .retry(2)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                .subscribe();
        Thread.sleep(10000);
    }
}
