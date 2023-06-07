package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.domain.service.MovieService;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesControllerTest {

    private static final String URL = "/movies";


    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    MovieService movieService;

    @Autowired
    ObjectMapper objectMapper;


    public static final MovieRequest VALID_MOVIE_REQUEST = new MovieRequest(
            "Star Wars - Clone Wars",
            "Very old film with Yoda and another heroes",
            Set.of("Yoda", "Luke Skywalker", "Anakin Skywalker"),
            2);

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void shouldCreateMovieSuccessWhenRequestIsValid() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Movie mockedMovie = new Movie(
                1,
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration(),
                now,
                now);

        when(movieService.save(any(Movie.class))).thenReturn(mockedMovie);

        String json = objectMapper.writeValueAsString(VALID_MOVIE_REQUEST);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(mockedMovie.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(mockedMovie.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value(mockedMovie.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(mockedMovie.getDuration()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors", hasItem("Yoda")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors", hasItem("Luke Skywalker")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors", hasItem("Anakin Skywalker")));
    }

}
