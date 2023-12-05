package dev.jb9.screenmatchapi.main;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SeriesDTO;
import dev.jb9.screenmatchapi.models.Episode;
import dev.jb9.screenmatchapi.services.OmdbApiService;
import dev.jb9.screenmatchapi.utils.Reader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final OmdbApiService omdbApiService = new OmdbApiService();
    private final Reader reader = new Reader();
    private List<Episode> episodes;

    public void execute() {
        printWelcomeMessage();

        String seriesName = reader.ask("What series do you want to watch?");
        omdbApiService.setSeriesName(seriesName);
        SeriesDTO seriesDTO = omdbApiService.fetchSeries();

        boolean shouldPrintAllEpisodes = reader.askForBoolean(
                "This series has a total of %s seasons. Do you want to list episodes from all of them?"
                        .formatted(seriesDTO.totalSeasons()));

        if (shouldPrintAllEpisodes) {
            setEpisodesFromAllSeasons(seriesDTO);
        } else {
            int chosenSeason = reader.askForInteger("Which season do you want to list the episodes?");
            setEpisodesFromSpecificSeason(chosenSeason);
        }

        if (episodes.isEmpty()) {
            System.out.println("Couldn't find any episode for " + seriesDTO.title());
            return;
        }

        Set<Integer> episodeAvailableYears = getEpisodeAvailableYears();
        if (episodeAvailableYears.isEmpty()) {
            System.out.println("These are all episodes found");
            episodes.forEach(System.out::println);
            return;
        }

        if (episodeAvailableYears.size() == 1) {
            List<Integer> years = new ArrayList<>(episodeAvailableYears);
            printEpisodesFromYear(years.get(0));
            return;
        }

        System.out.println("There are episodes for these years:");
        episodeAvailableYears.stream().sorted().forEach(episode -> System.out.print(episode + " "));
        int yearToFilter = reader.askForInteger("\nFrom which year do you want to list the episodes?");
        printEpisodesFromYear(yearToFilter);
    }

    private void printWelcomeMessage() {
        System.out.println("""
        Welcome to Screen Match API!
        Here you can find the series you want to watch in a easy way.
        """);
    }

    private void setEpisodesFromAllSeasons(SeriesDTO seriesDTO) {
        List<SeasonDTO> seasons = new ArrayList<>();
        for(int seasonNumber = 1; seasonNumber <= Integer.parseInt(seriesDTO.totalSeasons()); seasonNumber++) {
            SeasonDTO seasonDTO = fetchSeason(seasonNumber);
            seasons.add(seasonDTO);
        }

        episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(season.seasonNumber(), episode)))
                .toList();
    }

    private void setEpisodesFromSpecificSeason(Integer chosenSeason) {
        episodes = fetchSeason(chosenSeason).episodes().stream()
                .map(episodeDTO -> new Episode(chosenSeason.toString(), episodeDTO))
                .toList();
    }

    private SeasonDTO fetchSeason(int seasonNumber) {
        omdbApiService.setSeasonNumber(seasonNumber);
        return omdbApiService.fetchSeason();
    }

    private Set<Integer> getEpisodeAvailableYears() {
        Set<Integer> episodeAvailableYears = new HashSet<>();

        episodes.stream()
                .filter(episode -> episode.getReleasedAt() != null)
                .forEach(episode -> episodeAvailableYears.add(episode.getReleasedAt().getYear()));

        return episodeAvailableYears;
    }

    private void printEpisodesFromYear(int year) {
        LocalDate dateToFilter = LocalDate.of(year, 1, 1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("These are the episodes from year " + year);
        episodes.stream()
                .filter(episode -> episode.getReleasedAt() != null && episode.getReleasedAt().isAfter(dateToFilter))
                .forEach(episode -> System.out.println(
                                "Season: " + episode.getSeasonNumber()
                                        + " episode: " + episode.getEpisodeNumber()
                                        + " title: " + episode.getTitle()
                                        + " released at: " + episode.getReleasedAt().format(dateTimeFormatter)
                        )
                );

        printEpisodesStatistics();
    }

    private void printEpisodesStatistics() {
        System.out.println("#################### Here are some statistics ####################");
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
}
