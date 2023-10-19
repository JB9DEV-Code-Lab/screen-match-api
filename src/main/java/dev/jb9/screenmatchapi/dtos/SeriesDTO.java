package dev.jb9.screenmatchapi.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesDTO(
        @JsonAlias("Title")
        String title,
        String totalSeasons,
        String imdbRating
) {
}