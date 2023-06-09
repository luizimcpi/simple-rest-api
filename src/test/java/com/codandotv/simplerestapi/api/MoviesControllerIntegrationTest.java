package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.persistence.MovieRepository;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MoviesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    private static final String URL = "/movies";

    @BeforeEach
    void cleanDatabase(){
        movieRepository.deleteAll();
    }


    public static final MovieRequest VALID_MOVIE_REQUEST = new MovieRequest(
            "Star Wars - Clone Wars",
            "Very old film with Yoda and another heroes",
            Set.of("Yoda", "Luke Skywalker", "Anakin Skywalker"),
            2);

    public static final MovieRequest VALID_UPDATE_MOVIE_REQUEST = new MovieRequest(
            "Update Movie",
            "Updated Description",
            Set.of("Updated Actor", "Updated Actor 2"),
            1);


    @Test
    void shouldCreateMovieSuccessWhenRequestIsValid() throws Exception {
        String json = objectMapper.writeValueAsString(VALID_MOVIE_REQUEST);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(VALID_MOVIE_REQUEST.title()))
                .andExpect(jsonPath("description").value(VALID_MOVIE_REQUEST.description()))
                .andExpect(jsonPath("duration").value(VALID_MOVIE_REQUEST.duration()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors", hasSize(3)))
                .andExpect(jsonPath("$.actors", hasItem("Yoda")))
                .andExpect(jsonPath("$.actors", hasItem("Luke Skywalker")))
                .andExpect(jsonPath("$.actors", hasItem("Anakin Skywalker")))
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("updatedAt").exists());

    }

    @Test
    void shouldFindMovieByIdSuccessWhenIdIsValid() throws Exception {

        var movie = new Movie(
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration());

        var savedMovie = movieRepository.save(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL+"/"+savedMovie.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(savedMovie.getId().toString()))
                .andExpect(jsonPath("title").value(VALID_MOVIE_REQUEST.title()))
                .andExpect(jsonPath("description").value(VALID_MOVIE_REQUEST.description()))
                .andExpect(jsonPath("duration").value(VALID_MOVIE_REQUEST.duration()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors", hasSize(3)))
                .andExpect(jsonPath("$.actors", hasItem("Yoda")))
                .andExpect(jsonPath("$.actors", hasItem("Luke Skywalker")))
                .andExpect(jsonPath("$.actors", hasItem("Anakin Skywalker")))
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("updatedAt").exists());
    }

    @Test
    void shouldFindMovieByIdFailWhenIdNotExistsInDatabase() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL+"/"+UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldFindMoviesSuccessWhenExistsMoviesInDatabase() throws Exception {

        var movie = new Movie(
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration());

        movieRepository.save(movie);

        var anotherMovie = new Movie(
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration());

        movieRepository.save(anotherMovie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFindMoviesFailWhenNotExistsMoviesInDatabase() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldDeleteMovieByIdSuccessWhenFindMovieInDatabase() throws Exception {

        var movie = new Movie(
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration());

        var savedMovie = movieRepository.save(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(URL+"/"+savedMovie.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldDeleteMovieByIdFailWhenNotFindMovieInDatabase() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(URL+"/"+UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldUpdateMovieByIdSuccessWhenIdIsValid() throws Exception {

        String json = objectMapper.writeValueAsString(VALID_UPDATE_MOVIE_REQUEST);

        var movie = new Movie(
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration());

        var savedMovie = movieRepository.save(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(URL+"/"+savedMovie.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(savedMovie.getId().toString()))
                .andExpect(jsonPath("title").value(VALID_UPDATE_MOVIE_REQUEST.title()))
                .andExpect(jsonPath("description").value(VALID_UPDATE_MOVIE_REQUEST.description()))
                .andExpect(jsonPath("duration").value(VALID_UPDATE_MOVIE_REQUEST.duration()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors", hasSize(2)))
                .andExpect(jsonPath("$.actors", hasItem("Updated Actor")))
                .andExpect(jsonPath("$.actors", hasItem("Updated Actor 2")))
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("updatedAt").exists());
    }
}
