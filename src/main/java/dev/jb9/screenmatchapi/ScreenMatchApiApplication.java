package dev.jb9.screenmatchapi;

import dev.jb9.screenmatchapi.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Main mainApplicationClass = new Main();
        mainApplicationClass.execute();
    }
}
