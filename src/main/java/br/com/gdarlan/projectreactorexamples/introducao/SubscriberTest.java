package br.com.gdarlan.projectreactorexamples.introducao;

public class SubscriberTest {
    public static void main(String[] args) {
        YoutubeChannel publisher = new YoutubeChannel();
        publisher.addVideos(new Video("Reactive programming with Java",
                "This videos talk about reative programming", 200, 1000));
        publisher.addVideos(new Video("Java vs Kotlin",
                "This videos compare the difference about Java and Kotlin", 50, 2000));

//        User subscriber = new User("Gabriel");
//        publisher.getAllVideos().subscribeWith(subscriber);

//        publisher.getAllVideos().log().subscribe();

//        publisher.getAllVideos().log().subscribe(video -> System.out.println(video.getName()));
        publisher.getAllVideos().log().subscribe(video -> System.out.println(video.getName()),
                throwable -> System.out.println(throwable),
                () -> System.out.println("dados consumidos com sucessos")
        );
    }
}
