package com.codandotv.simplerestapi.domain.service;

import com.codandotv.simplerestapi.domain.exception.NoContentException;
import com.codandotv.simplerestapi.persistence.MovieInMemoryRepository;
import com.codandotv.simplerestapi.persistence.MovieRepository;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieInMemoryRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> findAll() {
        List<Movie> movies = movieRepository.findAll();
        if(movies.isEmpty()) throw new NoContentException("Movies list is empty");
        return movieRepository.findAll();
    }

    public Movie findById(UUID id){
        return movieRepository.findById(id);
    }
}
