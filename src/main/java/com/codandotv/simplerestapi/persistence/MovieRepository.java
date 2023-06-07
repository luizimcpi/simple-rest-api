package com.codandotv.simplerestapi.persistence;

import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface MovieRepository {

    Movie save(Movie movie);

    Optional<Movie> findByTitle(String title);
}
