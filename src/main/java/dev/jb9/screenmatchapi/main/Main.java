package dev.jb9.screenmatchapi.main;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SerieDTO;
import dev.jb9.screenmatchapi.models.Episode;
import dev.jb9.screenmatchapi.models.Season;
import dev.jb9.screenmatchapi.models.Serie;
import dev.jb9.screenmatchapi.services.OmdbApiService;
import dev.jb9.screenmatchapi.utils.Reader;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private final OmdbApiService OMDB_API_SERVICE = new OmdbApiService();
    private final Reader READER = new Reader();
    private final List<Serie> series = new ArrayList<>();
    private Serie serie;

    public void execute() {
        printWelcomeMessage();
        boolean wantToWatchSomething;
        do {
            String seriesName = READER.ask("What series do you want to watch?");
            OMDB_API_SERVICE.setSeriesName(seriesName);
            SerieDTO serieDTO = OMDB_API_SERVICE.fetchSeries();
            serie = new Serie(serieDTO);
            series.add(serie);

            boolean shouldPrintAllSeasons = READER.askForBoolean(
                    "This series has a total of %s seasons. Do you want to list episodes from all of them?"
                            .formatted(serieDTO.totalSeasons()));

            if (shouldPrintAllSeasons) {
                setAllSeasons(serie.getTotalSeasons());
            } else {
                int[] validOptions = {1, serie.getTotalSeasons()};
                setSpecificSeason(
                        READER.askForInteger("Which season do you want to list the episodes?", validOptions)
                );
            }

            if (serie.getTotalEpisodes() == 0) {
                System.out.println("Couldn't find any episode for " + serie.getTitle());
                return;
            }

            wantToWatchSomething = READER.askForBoolean("Do you want to choose another series to watch?");
        } while(wantToWatchSomething);

        series.forEach(listedSerie -> {
            printSeparator(listedSerie.getTitle().toUpperCase() + " DETAILS:", " ");
            listedSerie.getSeasonList().forEach(this::printEpisodesFromASeason);
        });
    }

    private void printWelcomeMessage() {
        printSeparator("", "#");
        System.out.print("""
        Welcome to Screen Match API!
        Here you can find the series you want to watch in a easy way.""");
        printSeparator("", "#");
    }

    private void setAllSeasons(int totalSeasons) {
        for(int seasonNumber = 1; seasonNumber <= totalSeasons; seasonNumber++) {
            setSpecificSeason(seasonNumber);
        }
    }

    private void setSpecificSeason(Integer chosenSeason) {
        serie.addSeason(new Season(fetchSeason(chosenSeason)));
    }

    private SeasonDTO fetchSeason(int seasonNumber) {
        OMDB_API_SERVICE.setSeasonNumber(seasonNumber);
        return OMDB_API_SERVICE.fetchSeason();
    }

    private void printEpisodesFromASeason(Season season) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        printSeparator("Episodes:", "#");
        season.getEpisodeList().forEach(episode -> {
                    String releasedAt = "";
                    if (episode.getReleasedAt() != null) {
                        releasedAt = " released at: %s".formatted(episode.getReleasedAt().format(dateTimeFormatter));
                    }
                    System.out.println(
                            "Season: " + episode.getSeasonNumber()
                                    + " episode: " + episode.getEpisodeNumber()
                                    + " title: " + episode.getTitle()
                                    + releasedAt
                    );
                }
                );

        printEpisodesStatistics(season.getEpisodeList());
    }

    private void printEpisodesStatistics(List<Episode> episodes) {
        printSeparator("Here are some statistics:", "#");
        Map<Integer, Double> ratingBySeason = episodes.stream()
                .filter(epsisode -> epsisode.getImdbRating() > 0)
                .collect(Collectors.groupingBy(
                        Episode::getSeasonNumber, Collectors.averagingDouble(Episode::getImdbRating)));

        System.out.println("Rating by season:");
        System.out.println(ratingBySeason);

        DoubleSummaryStatistics episodesStatistics = episodes.stream()
                .filter(episode -> episode.getImdbRating() > 0)
                .collect(Collectors.summarizingDouble(Episode::getImdbRating));

        System.out.println("Episode rating statistics");
        System.out.println("Episode count: " + episodesStatistics.getCount());
        System.out.println("Best rated episode: " + episodesStatistics.getMax());
        System.out.println("Worst rated episode: " + episodesStatistics.getMin());
        System.out.println("Episodes rating average: " + episodesStatistics.getAverage());
    }

    private void printSeparator(String message, String separatorChar) {
        int maxLineLength = 120;
        if (message.length() > maxLineLength) {
            System.out.println(message);
            return;
        }

        StringBuilder separatorComplement = new StringBuilder();
        separatorComplement.append(separatorChar.repeat((maxLineLength - message.length()) / 2)).append(" ");

        System.out.println("\n" + separatorComplement + message + separatorComplement.reverse());
    }
}
