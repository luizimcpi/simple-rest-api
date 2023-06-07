package com.codandotv.simplerestapi.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record MovieRequest(

        @NotNull(message = "Field 'title' not present in payload")
        @NotEmpty(message = "Field 'title' can´t be empty")
        String title,

        @NotNull(message = "Field 'description' not present in payload")
        @NotEmpty(message = "Field 'description' can´t be empty")
        String description,
        @NotNull(message = "Field 'actors' not present in payload")
        Set<String> actors,
        @NotNull(message = "Field 'duration' not present in payload")
        @Min(value = 1, message = "Min value for field 'duration' is 1")
        Integer duration) {

        public MovieRequest(String description, Set<String> actors, Integer duration) {
                this(null, description, actors, duration);
        }
}
