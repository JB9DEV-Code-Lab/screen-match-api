package dev.jb9.screenmatchapi.main;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.services.JsonSerializerService;
import dev.jb9.screenmatchapi.services.RequestAPIService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private final RequestAPIService requestAPI = new RequestAPIService();
    private final JsonSerializerService jsonSerializer = new JsonSerializerService();
    private final Scanner reader = new Scanner(System.in);
    public void execute() {
        StringBuilder endpoint = new StringBuilder("https://www.omdbapi.com?apikey=" + System.getenv("OMDB_API_KEY"));
        String seasonNumber = "1";
        System.out.println("""
                Welcome to Screen Match API!
                Here you can find the series you want to watch in a easy way. So, what series do you want to watch?""");

        String seriesName = reader.nextLine();
        endpoint.append("&t=").append(URLEncoder.encode(seriesName, StandardCharsets.UTF_8)).append("&season=")
                .append(seasonNumber);
        String seasonData = requestAPI.getData(endpoint.toString());
        SeasonDTO seasonJson = jsonSerializer.deserialize(seasonData, SeasonDTO.class);

        System.out.println("So, here are the episodes from season " + seasonNumber);
        seasonJson.episodes().forEach(
                episode -> System.out.println(" > episode " + episode.episodeNumber() + ": " + episode.title())
        );
    }
}
