package dev.jb9.screenmatchapi.models;

import dev.jb9.screenmatchapi.dtos.EpisodeDTO;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episode {
    // region fields
    private int seasonNumber;
    private String title;
    private int episodeNumber;
    private LocalDate releasedAt;
    private double imdbRating;
    // endregion fields

    // region constructors
    public Episode() {}

    public Episode(String seasonNumber, EpisodeDTO episodeDTO) {
        this.title = episodeDTO.title();
        try {
            this.episodeNumber = Integer.parseInt(episodeDTO.episodeNumber());
        } catch (NumberFormatException exception) {
            this.episodeNumber = 1;
        }

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
    // endregion constructors

    // region getters
    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public LocalDate getReleasedAt() {
        return releasedAt;
    }

    public double getImdbRating() {
        return imdbRating;
    }
    // endregion getters

    // region overridings
    @Override
    public String toString() {
        return "seasonNumber=" + seasonNumber +
                ", title='" + title + '\'' +
                ", episodeNumber='" + episodeNumber + '\'' +
                ", releasedAt=" + releasedAt +
                ", imdbRating=" + imdbRating;
    }
    // endregion overridings
}
