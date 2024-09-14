package br.com.gdarlan.projectreactorexamples.operadores;

import br.com.gdarlan.projectreactorexamples.introducao.Video;
import br.com.gdarlan.projectreactorexamples.introducao.YoutubeChannel;
import br.com.gdarlan.projectreactorexamples.mock.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class OperatorsTest {
    @Test
    void printVideos() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel
                .getAllVideos()
                .log()
                .subscribe(video -> System.out.println(video.getName()));
    }

    @Test
    void printVideosTake() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel
                .getAllVideos(2)
                .subscribe(video -> System.out.println(video.getName()));
    }

    @Test
    void printVideosTakeWhile() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel
                .getAllVideos()
                .log()
                .takeWhile(video -> video.getLikes() > 10)
                .subscribe(video -> System.out.println(video.getName()));
    }

    @Test
    void printDescriptionSize() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        youtubeChannel.getDescriptionSize().log().subscribe(System.out::println);
    }

    @Test
    void printVideoName() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        youtubeChannel.getAllVideosName().log().subscribe();
    }

    @Test
    void badFlatMap() {
        List<YoutubeChannel> channelList = Arrays.asList(
                new YoutubeChannel(MockVideo.generateVideos()),
                new YoutubeChannel(MockVideo.generateVideos2())
        );

        Flux<YoutubeChannel> channelFlux = Flux.fromIterable(channelList);
        channelFlux.map(video -> video.getAllVideosName())
                .log()
                .subscribe(item -> System.out.println(item));
    }

    @Test
    void flatMapVideosName() {
        List<YoutubeChannel> channelList = Arrays.asList(
                new YoutubeChannel(MockVideo.generateVideos()),
                new YoutubeChannel(MockVideo.generateVideos2())
        );

        Flux<YoutubeChannel> channelFlux = Flux.fromIterable(channelList);
        channelFlux.
                flatMap(video -> video.getAllVideosName())
                .log()
                .subscribe();
    }

    @Test
    void testFlatMapLike() {
        List<Video> videos = MockVideo.generateVideos3();
        YoutubeChannel youtubeChannel = new YoutubeChannel(videos);

        Flux<Integer> videoFlux = youtubeChannel
                .getAllVideos()
                .flatMap(Video::like)
                .map(Video::getLikes);

        StepVerifier
                .create(videoFlux)
                .expectNext(videos.get(0).getLikes() + 1,
                        videos.get(1).getLikes() + 1,
                        videos.get(2).getLikes() + 1)
                .verifyComplete();

    }

    @Test
    void filterByRating() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel
                .getVideosByRating(100)
                .subscribe(video -> System.out.println(video.getName() + " " + video.getLikes()));
    }

    @Test
    void testPrintVideosWithDelay() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos2());

        Flux<String> channels = youtubeChannel
                .getAllVideosName()
                .log()
                .delayElements(Duration.ofSeconds(2));

        StepVerifier
                .create(channels)
                .expectNext(
                        "What I need kafka? - 7",
                        "WebFlux pitfalls you need to know  - 8",
                        "ChatGPT integration with java - 9"
                ).verifyComplete();
    }

    @Test
    void printVideosWithDelay() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos2());

        youtubeChannel
                .getAllVideosName()
                .log()
                .delayElements(Duration.ofSeconds(2))
                .subscribe();

        Thread.sleep(5000);
    }

    @Test
    void simpleTransform() {
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(num -> num % 2 == 0)
                .transform(squareNumber())
                .subscribe(result -> System.out.println(" = " + result));

        System.out.println("");

        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(num -> num % 2 != 0)
                .transform(squareNumber())
                .subscribe(result -> System.out.println(" = " + result));
    }

    private Function<Flux<Integer>, Flux<Integer>> squareNumber() {
        return flux -> flux
                .doOnNext(num -> System.out.print("Square of " + num))
                .map(num -> num * num);
    }

    @Test
    void transformExample() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel
                .getAllVideos()
                .transform(transformMethod())
                .subscribe(System.out::println);
    }

    Function<Flux<Video>, Flux<String>> transformMethod() {
        return flux -> flux
                .filter(video -> video.getLikes() > 100)
                .map(video -> video.getName())
                .map(videoName -> videoName.toUpperCase());
    }

    @Test
    void sideEffects() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

//        youtubeChannel
//                .getAllVideos()
//                .log()
//                .doFirst(() -> System.out.println("doFirst"))
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnSubscribe((subscription -> System.out.println("doOnSubscribe")))
//                .subscribe();
//
//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnRequest((request) -> System.out.println("doOnRequest " + request))
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnNext((item) -> System.out.println("doOnNext " + item))
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnError((exception) -> System.out.println("doOnError " + exception))
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnCancel(() -> System.out.println("doOnCancel "))
//                .take(2)
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnComplete(() -> System.out.println("doOnComplete "))
//                .subscribe();

//        youtubeChannel
//                .getVideoName()
//                .log()
//                .doOnComplete(() -> System.out.println("doOnComplete "))
//                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
//                .subscribe();

        youtubeChannel
                .getAllVideosName()
                .log()
                .doOnComplete(() -> System.out.println("doOnComplete "))
                .doFinally(signalType -> System.out.println("doFinally " + signalType))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe();

    }
}
