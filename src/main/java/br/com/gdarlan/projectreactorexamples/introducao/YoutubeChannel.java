package br.com.gdarlan.projectreactorexamples.introducao;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel {
    private List<Video> videos;

    public YoutubeChannel(List<Video> videos) {
        this.videos = videos;
    }

    public YoutubeChannel() {
        this.videos = new ArrayList<>();
    }

    public void addVideos(Video video) {
        videos.add(video);
    }

    public Flux<Video> getAllVideos() {

        return Flux.fromIterable(videos);
    }

    public Flux<Video> getAllVideos(int number) {

        return Flux.fromIterable(videos).log().take(number);
    }

    public Flux<Integer> getDescriptionSize() {
        return getAllVideos().map(video -> video.getDescription().length());
    }

    public Flux<String> getAllVideosName() {
        return getAllVideos().map(Video::getName);
    }

    public Flux<Video> getVideosByRating(Integer rate) {
        return getAllVideos().filter(video -> video.getLikes() > rate);
    }


}
