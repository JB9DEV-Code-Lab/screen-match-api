package dev.jb9.screenmatchapi;

import dev.jb9.screenmatchapi.service.RequestAPIService;
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

		String titleName = URLEncoder.encode("furious fast", StandardCharsets.UTF_8);
		RequestAPIService requestAPIService = new RequestAPIService();
		String furiousFast = requestAPIService.getData(
				"https://www.omdbapi.com?t=" + titleName + "&apikey=" + System.getenv("OMDB_API_KEY")
		);

		System.out.println(furiousFast);
	}
}
