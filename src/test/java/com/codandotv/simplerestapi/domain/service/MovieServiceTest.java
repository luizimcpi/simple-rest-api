package com.codandotv.simplerestapi.domain.service;

import com.codandotv.simplerestapi.domain.exception.NoContentException;
import com.codandotv.simplerestapi.persistence.MovieRepository;
import com.codandotv.simplerestapi.persistence.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @InjectMocks
    MovieService service;

    @Mock
    MovieRepository repository;

    @Test
    void shouldSaveMovieSuccess(){
        ArgumentCaptor<Movie> movieParameter = ArgumentCaptor.forClass(Movie.class);

        var movie = new Movie(
                "test-title",
                "test-description",
                Set.of("test-actor-1"),
                1);

        service.save(movie);

        verify(repository).save(movieParameter.capture());

        assertEquals("test-title", movieParameter.getValue().getTitle());
        assertEquals("test-description", movieParameter.getValue().getDescription());
        assertFalse(movieParameter.getValue().getActors().isEmpty());
        assertEquals(1, movieParameter.getValue().getDuration());
    }

    @Test
    void shouldFindAllSuccess(){
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var mockedMovie = new Movie(
                id,
                "test-title",
                "test-description",
                Set.of("test-actor-1"),
                1,
                now,
                now);

        when(repository.findAll()).thenReturn(List.of(mockedMovie));

        var movies = service.findAll();

        assertFalse(movies.isEmpty());
        assertEquals(1, movies.size());
        assertEquals(id, movies.get(0).getId());
        assertEquals("test-title", movies.get(0).getTitle());
        assertEquals("test-description", movies.get(0).getDescription());
        assertEquals(1, movies.get(0).getDuration());
        assertEquals(now, movies.get(0).getCreatedAt());
        assertEquals(now, movies.get(0).getUpdatedAt());
    }

    @Test
    void shouldFindAllFailWhenMoviesIsEmpty(){
        when(repository.findAll()).thenReturn(List.of());

        assertThrows(NoContentException.class, () -> service.findAll());
    }
}
