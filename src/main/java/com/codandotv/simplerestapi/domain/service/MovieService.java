package com.codandotv.simplerestapi.domain.service;

import com.codandotv.simplerestapi.persistence.MovieRepository;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
}
