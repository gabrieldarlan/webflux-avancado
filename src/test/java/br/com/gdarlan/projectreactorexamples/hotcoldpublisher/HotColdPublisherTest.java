package br.com.gdarlan.projectreactorexamples.hotcoldpublisher;

import br.com.gdarlan.projectreactorexamples.introducao.VideoLive;
import br.com.gdarlan.projectreactorexamples.introducao.YoutubeChannel;
import br.com.gdarlan.projectreactorexamples.mock.MockVideo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class HotColdPublisherTest {

    @Test
    public void testColdPublisher() {
        YoutubeChannel youtubeChannel = new YoutubeChannel(MockVideo.generateVideos());

        youtubeChannel.getAllVideosName()
                .subscribe(value -> System.out.println("Subscribe 1 " + value));
        System.out.println("----------");
        youtubeChannel.getAllVideosName()
                .subscribe(value -> System.out.println("Subscribe 2 " + value));
    }

    @Test
    void testHotPublishAutoConnect() throws InterruptedException {
        VideoLive videoLive = new VideoLive("Meetup about Java");

        Flux<String> java21Live = videoLive.play();

        java21Live.subscribe(value -> System.out.println("Usuario 1 " + value));

        Thread.sleep(2000);

        java21Live.subscribe(value -> System.out.println("Usuario 2 " + value));

        Thread.sleep(4000);

        java21Live.subscribe(value -> System.out.println("Usuario 3 " + value));

        Thread.sleep(20_0000);
    }


    @Test
    void testHotPublishAutoConnectN() throws InterruptedException {
        VideoLive videoLive = new VideoLive("Meetup about Java");

        Flux<String> java21Live = videoLive.playN();

        Thread.sleep(2000);

        java21Live.subscribe(value -> System.out.println("Usuario 1 " + value));

        Thread.sleep(2000);

        java21Live.subscribe(value -> System.out.println("Usuario 2 " + value));

        Thread.sleep(4000);

        java21Live.subscribe(value -> System.out.println("Usuario 3 " + value));

        Thread.sleep(20_0000);
    }


    @Test
    void testHotPublishAutoConnectReSubscription() throws InterruptedException {
        VideoLive videoLive = new VideoLive("Meetup about Java");

        Flux<String> java21Live = videoLive.playResubscription();

        Thread.sleep(2000);

        java21Live.subscribe(value -> System.out.println("Usuario 1 " + value));

        Thread.sleep(2000);

        java21Live.subscribe(value -> System.out.println("Usuario 2 " + value));

        Thread.sleep(4000);

        java21Live.subscribe(value -> System.out.println("Usuario 3 " + value));

        Thread.sleep(20_0000);
    }

}
