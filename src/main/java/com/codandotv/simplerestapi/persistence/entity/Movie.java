package com.codandotv.simplerestapi.persistence.entity;

import java.time.LocalDateTime;
import java.util.Set;

public class Movie {

    private Integer id;
    private String title;
    private String description;
    private Set<String> actors;
    private Integer duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Movie() {
    }

    public Movie(String title, String description, Set<String> actors, Integer duration) {
        this.title = title;
        this.description = description;
        this.actors = actors;
        this.duration = duration;
    }

    public Movie(Integer id,
                 String title,
                 String description,
                 Set<String> actors,
                 Integer duration,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.actors = actors;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getActors() {
        return actors;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
