package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.persistence.MovieRepository;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MoviesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    private static final String URL = "/movies";

    public static final MovieRequest VALID_MOVIE_REQUEST = new MovieRequest(
            "Star Wars - Clone Wars",
            "Very old film with Yoda and another heroes",
            Set.of("Yoda", "Luke Skywalker", "Anakin Skywalker"),
            2);


    @Test
    void shouldCreateMovieSuccessWhenRequestIsValid() throws Exception {
        String json = objectMapper.writeValueAsString(VALID_MOVIE_REQUEST);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Optional<Movie> movie = movieRepository.findByTitle(VALID_MOVIE_REQUEST.title());

        Assertions.assertTrue(movie.isPresent());
        Assertions.assertEquals(1, movie.get().getId());
        Assertions.assertEquals("Star Wars - Clone Wars", movie.get().getTitle());
        Assertions.assertEquals("Very old film with Yoda and another heroes", movie.get().getDescription());
        Assertions.assertEquals(2, movie.get().getDuration());
        Assertions.assertEquals(3, movie.get().getActors().size());
        Assertions.assertNotNull(movie.get().getCreatedAt());
        Assertions.assertNotNull(movie.get().getUpdatedAt());
    }
}
