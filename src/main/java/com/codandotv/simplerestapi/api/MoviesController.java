package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.mapper.MovieMapper;
import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.api.response.MovieResponse;
import com.codandotv.simplerestapi.domain.service.MovieService;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        log.info("Initiating movie registration with request: {}", request.toString());

        var movie = new Movie();
        BeanUtils.copyProperties(request, movie);

        log.info("Request converted into entity: {}", movie);
        Movie savedMovie = movieService.save(movie);

        log.info("Movie has been registered successfully: {}", savedMovie.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(MovieMapper.toResponse(savedMovie));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll(){

        log.info("Initiating find all movies...");

        var movies = movieService.findAll();

        log.info("Movies has been found...");

        var moviesResponse = movies.stream()
                .map(MovieMapper::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(moviesResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> findById(@PathVariable("id") UUID id){

        log.info("Initiating search movie with id: {}", id);

        var movie = movieService.findById(id);

        log.info("Movies has been found...");

        return ResponseEntity.status(HttpStatus.OK).body(MovieMapper.toResponse(movie));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id){

        log.info("Deleting movie with id: {}", id);

        movieService.deleteById(id);

        log.info("Movie with id: {} has been deleted...", id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> deleteById(@PathVariable("id") UUID id,
                                                    @RequestBody @Valid MovieRequest request){

        log.info("Updating movie with id: {} and using request: {}", id, request);

        var movie = new Movie();
        BeanUtils.copyProperties(request, movie);

        var updatedMovie = movieService.update(id, movie);

        log.info("Movie with id: {} has been updated...", id);

        return ResponseEntity.status(HttpStatus.OK).body(MovieMapper.toResponse(updatedMovie));

    }

}
