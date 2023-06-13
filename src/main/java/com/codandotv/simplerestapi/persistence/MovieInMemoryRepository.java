package com.codandotv.simplerestapi.persistence;


import com.codandotv.simplerestapi.domain.exception.NotFoundException;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class MovieInMemoryRepository implements MovieRepository {

    private Map<UUID, Movie> tempMoviesDb = new HashMap<>();

    @Override
    public Movie save(Movie movie){
        LocalDateTime now = LocalDateTime.now();
        UUID movieId = UUID.randomUUID();
        Movie movieWithId = new Movie(
                movieId,
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

    @Override
    public Movie findById(UUID id) {
        return new ArrayList<>(tempMoviesDb.values())
                .stream().filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }

    @Override
    public void deleteById(UUID id) {
        this.findById(id);
        tempMoviesDb.remove(id);
    }

    @Override
    public Movie update(UUID id, Movie movie) {
        var savedMovie = this.findById(id);
        LocalDateTime updatedTime = LocalDateTime.now();
        Movie updatedMovie = new Movie(
                savedMovie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getActors(),
                movie.getDuration(),
                savedMovie.getCreatedAt(),
                updatedTime);
        tempMoviesDb.put(savedMovie.getId(), updatedMovie);
        return updatedMovie;
    }

    @Override
    public void deleteAll() {
        tempMoviesDb = new HashMap<>();
    }
}
