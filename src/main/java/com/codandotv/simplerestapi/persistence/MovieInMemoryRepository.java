package com.codandotv.simplerestapi.persistence;


import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class MovieInMemoryRepository implements MovieRepository {

    private Map<Integer, Movie> tempMoviesDb = new HashMap<>();
    private final AtomicInteger countId = new AtomicInteger(0);

    @Override
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
        return movieWithId;
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return new ArrayList<>(tempMoviesDb.values())
                .stream().filter(m -> m.getTitle().equals(title))
                .findFirst();
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(tempMoviesDb.values());
    }
}
