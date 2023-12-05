package dev.jb9.screenmatchapi.services;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;
import dev.jb9.screenmatchapi.dtos.SeriesDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OmdbApiService extends RequestAPIService {
    private final String BASE_URL = "https://www.omdbapi.com?apikey=" + System.getenv("OMDB_API_KEY");
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
    public SeriesDTO fetchSeries() {
        String endpoint = getSeriesPath();
        String seriesData = getData(endpoint);

        return jsonSerializer.deserialize(seriesData, SeriesDTO.class);
    }

    public SeasonDTO fetchSeason() {
        String seasonData = getData(getSeasonPath());
        return jsonSerializer.deserialize(seasonData, SeasonDTO.class);
    }
    // endregion public methods

    // region private methods
    private String getSeriesPath() {
        return BASE_URL + "&t=" + URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
    }

    private String getSeasonPath() {
        return getSeriesPath() + "&season=" + seasonNumber;
    }
    // endregion private methods
}
