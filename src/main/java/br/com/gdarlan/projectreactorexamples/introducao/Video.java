package br.com.gdarlan.projectreactorexamples.introducao;

import reactor.core.publisher.Mono;

public class Video {
    private String name;
    private String description;
    private Integer likes;
    private Integer views;

    public Video(String name, String description, Integer likes, Integer views) {
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.views = views;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Mono<Video> like() {
        this.likes++;
        return Mono.just(this);
    }
}
