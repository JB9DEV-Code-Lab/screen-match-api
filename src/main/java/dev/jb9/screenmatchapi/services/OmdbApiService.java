package dev.jb9.screenmatchapi.services;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SerieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OmdbApiService extends RequestAPIService {
    @Value("${omdb.baseUrl}")
    private String baseUrl;
    @Value("${omdb.apiKey}")
    private String apiKey;

    private final String BASE_URL = "https://www.omdbapi.com";
    private final JsonSerializerService jsonSerializer = new JsonSerializerService();

    private String seriesName;
    private int seasonNumber;

    // region setters
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }
    // endregion setters

    // region public methods
    public SerieDTO fetchSeries() {
        String endpoint = getSeriesPath();
        String seriesData = getData(endpoint);

        return jsonSerializer.deserialize(seriesData, SerieDTO.class);
    }

    public SeasonDTO fetchSeason() {
        String seasonData = getData(getSeasonPath());
        return jsonSerializer.deserialize(seasonData, SeasonDTO.class);
    }
    // endregion public methods

    // region private methods
    private String getEndpoint() {
        return baseUrl + "?apikey=" + apiKey;
    }
    private String getSeriesPath() {
        return getEndpoint() + "&t=" + URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
    }

    private String getSeasonPath() {
        return getSeriesPath() + "&season=" + seasonNumber;
    }
    // endregion private methods
}
