package dev.jb9.screenmatchapi.models;

import dev.jb9.screenmatchapi.dtos.EpisodeDTO;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episode {
    private int seasonNumber;
    private final String title;
    private final String episodeNumber;
    private LocalDate releasedAt;
    private double imdbRating;

    public Episode(String seasonNumber, EpisodeDTO episodeDTO) {
        this.title = episodeDTO.title();
        this.episodeNumber = episodeDTO.episodeNumber();

        try {
            this.seasonNumber = Integer.parseInt(seasonNumber);
        } catch (NumberFormatException exception) {
            this.seasonNumber = 1;
        }

        try {
            this.releasedAt = LocalDate.parse(episodeDTO.releasedAt());
        } catch (DateTimeException exception) {
            this.releasedAt = null;
        }

        try {
            this.imdbRating = Double.parseDouble(episodeDTO.imdbRating());
        } catch (NumberFormatException exception) {
            this.imdbRating = 0.0;
        }
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public LocalDate getReleasedAt() {
        return releasedAt;
    }

    @Override
    public String toString() {
        return "seasonNumber=" + seasonNumber +
                ", title='" + title + '\'' +
                ", episodeNumber='" + episodeNumber + '\'' +
                ", releasedAt=" + releasedAt +
                ", imdbRating=" + imdbRating;
    }
}
