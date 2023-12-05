package dev.jb9.screenmatchapi.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeDTO (
        @JsonAlias("Title") String title,
        @JsonAlias("Episode") String episodeNumber,
        @JsonAlias("Released") String releasedAt,
        String imdbRating
) {}
