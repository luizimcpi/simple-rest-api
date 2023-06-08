package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.request.MovieRequest;
import com.codandotv.simplerestapi.domain.exception.NoContentException;
import com.codandotv.simplerestapi.domain.exception.NotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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


    private static final MovieRequest VALID_MOVIE_REQUEST = new MovieRequest(
            "Star Wars - Clone Wars",
            "Very old film with Yoda and another heroes",
            Set.of("Yoda", "Luke Skywalker", "Anakin Skywalker"),
            2);

    private static final MovieRequest INVALID_MOVIE_REQUEST_WITHOUT_TITLE = new MovieRequest(
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
        var mockedMovie = new Movie(
                UUID.randomUUID(),
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
                .andExpect(jsonPath("id").value(mockedMovie.getId().toString()))
                .andExpect(jsonPath("title").value(mockedMovie.getTitle()))
                .andExpect(jsonPath("description").value(mockedMovie.getDescription()))
                .andExpect(jsonPath("duration").value(mockedMovie.getDuration()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors", hasSize(3)))
                .andExpect(jsonPath("$.actors", hasItem("Yoda")))
                .andExpect(jsonPath("$.actors", hasItem("Luke Skywalker")))
                .andExpect(jsonPath("$.actors", hasItem("Anakin Skywalker")));
    }

    @Test
    void shouldCreateMovieFailWhenRequestWithoutFieldTitleInRequest() throws Exception {

        String json = objectMapper.writeValueAsString(INVALID_MOVIE_REQUEST_WITHOUT_TITLE);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("errorCode").value((HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("status").value((HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem("Field: 'title' Message: Field 'title' not present in payload")))
                .andExpect(jsonPath("$.errors", hasItem("Field: 'title' Message: Field 'title' can´t be empty")));
    }


    @Test
    void shouldReturnMoviesWhenExistsInDatabase() throws Exception {


        LocalDateTime now = LocalDateTime.now();
        var mockedMovie = new Movie(
                UUID.randomUUID(),
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration(),
                now,
                now);

        var anotherMockedMovie = new Movie(
                UUID.randomUUID(),
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration(),
                now,
                now);

        when(movieService.findAll()).thenReturn(List.of(mockedMovie, anotherMockedMovie));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturnNoContentsWhenNotExistsMoviesInDatabase() throws Exception {

        when(movieService.findAll()).thenThrow(NoContentException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldFindMovieByIdSuccessWhenItIsInDatabase() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        var movieId = UUID.randomUUID();
        var mockedMovie = new Movie(
                movieId,
                VALID_MOVIE_REQUEST.title(),
                VALID_MOVIE_REQUEST.description(),
                VALID_MOVIE_REQUEST.actors(),
                VALID_MOVIE_REQUEST.duration(),
                now,
                now);

        when(movieService.findById(any())).thenReturn(mockedMovie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL+"/"+movieId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(mockedMovie.getId().toString()))
                .andExpect(jsonPath("title").value(mockedMovie.getTitle()))
                .andExpect(jsonPath("description").value(mockedMovie.getDescription()))
                .andExpect(jsonPath("duration").value(mockedMovie.getDuration()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors", hasSize(3)))
                .andExpect(jsonPath("$.actors", hasItem("Yoda")))
                .andExpect(jsonPath("$.actors", hasItem("Luke Skywalker")))
                .andExpect(jsonPath("$.actors", hasItem("Anakin Skywalker")));
    }

    @Test
    void shouldReturnNotFoundWhenNotExistsMovieWithIdInDatabase() throws Exception {

        when(movieService.findById(any())).thenThrow(NotFoundException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL+"/"+UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
