package com.codandotv.simplerestapi.api.request;

import java.util.List;
import java.util.Set;

public record MovieRequest(
        String title,
        String description,
        Set<String> actors,
        Integer duration) {
}
