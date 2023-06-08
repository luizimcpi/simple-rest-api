package com.codandotv.simplerestapi.persistence;

import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface MovieRepository {

    Movie save(Movie movie);

    Optional<Movie> findByTitle(String title);

    List<Movie> findAll();

    Movie findById(UUID id);
}
