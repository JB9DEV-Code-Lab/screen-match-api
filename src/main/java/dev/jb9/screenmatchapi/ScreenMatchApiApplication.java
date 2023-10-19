package dev.jb9.screenmatchapi;

import dev.jb9.screenmatchapi.dtos.SeriesDTO;
import dev.jb9.screenmatchapi.exceptions.JsonSerializerException;
import dev.jb9.screenmatchapi.services.JsonSerializerService;
import dev.jb9.screenmatchapi.services.RequestAPIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ScreenMatchApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Just following the course instructions, to implement a command line runner here");

		String titleName = URLEncoder.encode("the big bang theory", StandardCharsets.UTF_8);
		RequestAPIService requestAPIService = new RequestAPIService();
		String titleData = requestAPIService.getData(
				"https://www.omdbapi.com?t=" + titleName + "&apikey=" + System.getenv("OMDB_API_KEY")
		);

		try {
		JsonSerializerService jsonSerializerService = new JsonSerializerService();
		SeriesDTO json = jsonSerializerService.deserialize(titleData, SeriesDTO.class);

		System.out.println("raw data from OMDB API: " + titleData);
		System.out.println("deserialized data from service: " + json);
		} catch (JsonSerializerException exception) {
			System.out.printf("Couldn't deserialize due to %s", exception.getMessage());
		}
	}
}
