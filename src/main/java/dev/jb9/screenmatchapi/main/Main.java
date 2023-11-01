package dev.jb9.screenmatchapi.main;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SeriesDTO;
import dev.jb9.screenmatchapi.models.Episode;
import dev.jb9.screenmatchapi.services.JsonSerializerService;
import dev.jb9.screenmatchapi.services.RequestAPIService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private final RequestAPIService requestAPI = new RequestAPIService();
    private final JsonSerializerService jsonSerializer = new JsonSerializerService();
    private final Scanner reader = new Scanner(System.in);
    private final StringBuilder endpoint = new StringBuilder(
            "https://www.omdbapi.com?apikey=" + System.getenv("OMDB_API_KEY"));
    private List<Episode> episodes;

    public void execute() {
        System.out.println("""
        Welcome to Screen Match API!
        Here you can find the series you want to watch in a easy way. So, what series do you want to watch?
        """);

        String seriesName = reader.nextLine();
        endpoint.append("&t=").append(URLEncoder.encode(seriesName, StandardCharsets.UTF_8));
        String seriesData = requestAPI.getData(endpoint.toString());
        SeriesDTO seriesDTO = jsonSerializer.deserialize(seriesData, SeriesDTO.class);

        System.out.printf("""
        This series has a total of %s seasons. Do you want to list episodes from all of them? [y / n]
        """, seriesDTO.totalSeasons());
        String printAllSeasonEpisodes = reader.nextLine();

        if (printAllSeasonEpisodes.equalsIgnoreCase("y") || printAllSeasonEpisodes.equalsIgnoreCase("yes")) {
            setEpisodesFromAllSeasons(seriesDTO);
        } else {
            System.out.println("Which season do you want to list the episodes?");
            int chosenSeason = reader.nextInt();
            reader.nextLine();
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
        System.out.println("\nFrom which year do you want to list the episodes?");
        int yearToFilter = reader.nextInt();
        reader.nextLine();

        printEpisodesFromYear(yearToFilter);
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
        String seasonData = requestAPI.getData(endpoint + "&season=" + seasonNumber);
        return jsonSerializer.deserialize(seasonData, SeasonDTO.class);
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
        episodes.stream()
                .filter(episode -> episode.getReleasedAt() != null && episode.getReleasedAt().isAfter(dateToFilter))
                .forEach(episode -> System.out.println(
                                "Season: " + episode.getSeasonNumber()
                                        + " episode: " + episode.getEpisodeNumber()
                                        + " title: " + episode.getTitle()
                                        + " released at: " + episode.getReleasedAt().format(dateTimeFormatter)
                        )
                );
    }
}
