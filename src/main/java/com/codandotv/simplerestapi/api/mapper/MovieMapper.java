package com.codandotv.simplerestapi.api.mapper;

import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.api.response.MovieResponse;
import com.codandotv.simplerestapi.persistence.entity.Movie;

import java.time.LocalDateTime;

public final class MovieMapper {

    public static MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getActors(),
                movie.getDuration(),
                movie.getCreatedAt(),
                movie.getUpdatedAt());
    }
}
