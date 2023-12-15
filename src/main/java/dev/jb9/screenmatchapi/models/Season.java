package dev.jb9.screenmatchapi.models;

import dev.jb9.screenmatchapi.dtos.SeasonDTO;

import java.util.ArrayList;
import java.util.List;

public class Season {
    // region fields
    private int seasonNumber;
    private final List<Episode> EPISODE_LIST = new ArrayList<>();
    // endregion fields

    // region contructors
    public Season() {}
    public Season(SeasonDTO seasonDTO) {
        try {
            this.seasonNumber = Integer.parseInt(seasonDTO.seasonNumber());
        } catch (NumberFormatException exception) {
            this.seasonNumber = 1;
        }

        seasonDTO.episodes().forEach(episode -> this.addEpisode(new Episode(seasonDTO.seasonNumber(), episode)));
    }
    // endregion contructors

    // region getters
    public int getSeasonNumber() {
        return seasonNumber;
    }

    public List<Episode> getEpisodeList() {
        return EPISODE_LIST;
    }
    // endregion getters

    // region setters
    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void addEpisode(Episode episode) {
        this.EPISODE_LIST.add(episode);
    }
    // endregion setters
}
