package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.mapper.MovieMapper;
import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.api.response.MovieResponse;
import com.codandotv.simplerestapi.domain.service.MovieService;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@Validated
public class MoviesController {

    Logger log = LoggerFactory.getLogger(MoviesController.class);

    private final MovieService movieService;

    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@RequestBody @Valid MovieRequest request){

        log.info("Initiating movie registration with request: {}", request);

        Movie movie = movieService.save(MovieMapper.toEntity(request));

        log.info("Movie has been registered successfully: {}", movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(MovieMapper.toResponse(movie));
    }

}
