package kr.megaptera.smash.dtos;

import java.util.List;

public class PlaceSearchResultsDto {
    private final List<PlaceSearchResultDto> searchedPlaces;

    public PlaceSearchResultsDto(List<PlaceSearchResultDto> searchedPlaces) {
        this.searchedPlaces = searchedPlaces;
    }

    public List<PlaceSearchResultDto> getSearchedPlaces() {
        return searchedPlaces;
    }
}
