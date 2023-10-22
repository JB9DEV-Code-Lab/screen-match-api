package dev.jb9.screenmatchapi.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonDTO(
        @JsonAlias("Season")
        String seasonNumber,
        @JsonAlias("Episodes")
        List<EpisodeDTO> episodes
) {}
