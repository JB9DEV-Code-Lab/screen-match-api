package dev.jb9.screenmatchapi.models;

import dev.jb9.screenmatchapi.dtos.SerieDTO;

public class Serie {
    private String title;
    private int totalSeasons;
    private double imdbRating;
    private String genre;
    private String actors;
    private String poster;
    private String synopsis;

    public Serie() {}
    public Serie(SerieDTO serieDTO) {
        this.title = serieDTO.title();
        this.genre = serieDTO.genre();
        this.actors = serieDTO.actors();
        this.poster = serieDTO.poster();
        this.synopsis = serieDTO.synopsis();

        try {
            this.totalSeasons = Integer.parseInt(serieDTO.totalSeasons());
        } catch(NumberFormatException exception) {
            this.totalSeasons = 1;
        }

        try {
            this.imdbRating = Double.parseDouble(serieDTO.imdbRating());
        } catch (NumberFormatException exception) {
            this.imdbRating = 0.0;
        }
    }

    // region getters
    public String getTitle() {
        return title;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public String getSynopsis() {
        return synopsis;
    }
    // endregion getters

    // region setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalSeasons(int totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    // endregion setters
}
