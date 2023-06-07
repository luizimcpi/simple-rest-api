package com.codandotv.simplerestapi.persistence;


import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MovieRepository {

    private Map<Integer, Movie> tempMoviesDb = new HashMap<>();
    private final AtomicInteger countId = new AtomicInteger(0);

    public Movie save(Movie movie){
        Integer movieId = countId.incrementAndGet();
        LocalDateTime now = LocalDateTime.now();
        Movie movieWithId = new Movie(movieId,
                movie.getTitle(),
                movie.getDescription(),
                movie.getActors(),
                movie.getDuration(),
                now,
                now);
        tempMoviesDb.put(movieId, movieWithId);
        return movie;
    }
}
