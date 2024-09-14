package br.com.gdarlan.projectreactorexamples.scheduler;

import br.com.gdarlan.projectreactorexamples.introducao.VideoAnalyser;
import br.com.gdarlan.projectreactorexamples.introducao.YoutubeChannel;
import br.com.gdarlan.projectreactorexamples.mock.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersTest {

    @Test
    void blockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        Flux<String> videos = youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .map(video -> {
                    System.out.println("Map - Thread + " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .map(description -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Map 2 - Thread: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();
                });


        for (int i = 0; i < 2; i++) {
            System.out.println("Execucao " + i);
            videos.subscribe(System.out::println);
        }
        Thread.sleep(20_000);
    }


    @Test
    void publishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        Flux<String> videos = youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .map(video -> {
                    System.out.println("Map - Thread + " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .publishOn(Schedulers.boundedElastic())
                .map(description -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Map 2 - Thread: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();
                });

//        videos.subscribe(System.out::println);

        for (int i = 0; i < 20; i++) {
            System.out.println("Execucao " + i);
            videos.subscribe(System.out::println);
        }
        Thread.sleep(20_000);
    }

    @Test
    void subscribeOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos3());

        Flux<String> videos = youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .map(video -> {
                    System.out.println("Map - Thread + " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(description -> {
                    try {
                        Thread.sleep(5000);
                        System.out.println("Map 2 - Thread: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return description.toUpperCase();
                });

//        videos.subscribe(System.out::println);

        for (int i = 0; i < 2; i++) {
            System.out.println("Execucao " + i);
            videos.subscribe(System.out::println);
        }
        Thread.sleep(20_000);
    }

    @Test
    void parallelBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();

        youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter 1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
//                .publishOn(Schedulers.parallel())
                .parallel().runOn(Schedulers.boundedElastic())
                .map(video -> videoAnalyser.analyseBlocking(video))
                .subscribe();

        Thread.sleep(3_000);
    }

    @Test
    void parallelPublishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());
        VideoAnalyser videoAnalyser = new VideoAnalyser();

        youtubeChannel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter 1 - Thread " + Thread.currentThread().getName());
                    return video.getDescription().length() > 10;
                })
                .flatMap(videoAnalyser::analyseBlockingMono)
                .subscribe();

        Thread.sleep(3_000);
    }


}
