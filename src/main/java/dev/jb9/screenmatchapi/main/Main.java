package dev.jb9.screenmatchapi.main;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SerieDTO;
import dev.jb9.screenmatchapi.models.Episode;
import dev.jb9.screenmatchapi.services.OmdbApiService;
import dev.jb9.screenmatchapi.utils.Reader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final OmdbApiService OMDB_API_SERVICE = new OmdbApiService();
    private final Reader READER = new Reader();
    private final List<SeasonDTO> SEASONS = new ArrayList<>();
    private List<Episode> episodes;

    public void execute() {
        printWelcomeMessage();
        boolean wantToWatchSomething = true;
        do {
            String seriesName = READER.ask("What series do you want to watch?");
            OMDB_API_SERVICE.setSeriesName(seriesName);
            SerieDTO serieDTO = OMDB_API_SERVICE.fetchSeries();

            boolean shouldPrintAllEpisodes = READER.askForBoolean(
                    "This series has a total of %s seasons. Do you want to list episodes from all of them?"
                            .formatted(serieDTO.totalSeasons()));

            if (shouldPrintAllEpisodes) {
                setEpisodesFromAllSeasons(serieDTO);
            } else {
                int chosenSeason = READER.askForInteger("Which season do you want to list the episodes?");
                setEpisodesFromSpecificSeason(chosenSeason);
            }

            if (episodes.isEmpty()) {
                /* TODO: may be changed to look into series instead of episodes */
                System.out.println("Couldn't find any episode for " + serieDTO.title());
                return;
            }

            /*
            * TODO: All of this episodeAvailableYears related stuff may be removed, since it won't make sense, when
            *  looping though series
            * */
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

            wantToWatchSomething = READER.askForBoolean("Do you want to choose another series to watch?");

            /*
            * TODO: This code down may be removed, since the seasons are related with the years, so if the user chose
            *  the season before we'll have only one to print when loop through it, other wise we'll have many
            * */
            System.out.println("There are episodes for these years:");
            episodeAvailableYears.stream().sorted().forEach(episode -> System.out.print(episode + " "));
            int yearToFilter = READER.askForInteger("\nFrom which year do you want to list the episodes?");
            printEpisodesFromYear(yearToFilter);
        } while(wantToWatchSomething);

        /*
         TODO: Should loop through series and call "printEpisodesFromYear" passing the yearToFilter and the episode from
          the iteration
         */
    }

    private void printWelcomeMessage() {
        System.out.println("""
        Welcome to Screen Match API!
        Here you can find the series you want to watch in a easy way.
        """);
    }

    private void setEpisodesFromAllSeasons(SerieDTO serieDTO) {
        /*
            TODO: This may be changed to fetchAllSeasons and it should be all stored in one series model and then added
            to a series list
        */
        for(int seasonNumber = 1; seasonNumber <= Integer.parseInt(serieDTO.totalSeasons()); seasonNumber++) {
            SeasonDTO seasonDTO = fetchSeason(seasonNumber);
            SEASONS.add(seasonDTO);
        }

        episodes = SEASONS.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(season.seasonNumber(), episode)))
                .toList();
    }

    private void setEpisodesFromSpecificSeason(Integer chosenSeason) {
        /*
        * TODO: This will probably be removed, since at the end the loop will parse through series, which will have
        *  a relation with seasons which will have a relation with episodes, to print it all
        * */
        SeasonDTO season = fetchSeason(chosenSeason);
        SEASONS.add(season);
        episodes = season.episodes().stream()
                .map(episodeDTO -> new Episode(chosenSeason.toString(), episodeDTO))
                .toList();
    }

    private SeasonDTO fetchSeason(int seasonNumber) {
        OMDB_API_SERVICE.setSeasonNumber(seasonNumber);
        return OMDB_API_SERVICE.fetchSeason();
    }

    private Set<Integer> getEpisodeAvailableYears() {
        /*
        * TODO: Here it should probably receive "List<Episode> episodes" as a parameter instead of using episodes field
        *  */
        Set<Integer> episodeAvailableYears = new HashSet<>();

        episodes.stream()
                .filter(episode -> episode.getReleasedAt() != null)
                .forEach(episode -> episodeAvailableYears.add(episode.getReleasedAt().getYear()));

        return episodeAvailableYears;
    }

    private void printEpisodesFromYear(int year) {
        /*
        * TODO: Here episodes should be received as a parameter, since series will be stored in a list, from where it'll
        *  be used to print all at the end
        * */
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
        /*
         * TODO: Here episodes should be received as a parameter, since series will be stored in a list, from where it'll
         *  be used to print all at the end
         * */
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
