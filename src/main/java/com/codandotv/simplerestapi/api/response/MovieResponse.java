package com.codandotv.simplerestapi.api.response;

import java.time.LocalDateTime;
import java.util.Set;

public record MovieResponse(Integer id,
                            String title,
                            String description,
                            Set<String> actors,
                            Integer duration,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt) {

    public MovieResponse() {
        this(null, null, null, null, null, null, null);
    }

}
