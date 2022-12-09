package kr.megaptera.smash.dtos;

import java.util.List;

public class PlacesDto {
    private final List<PlaceDto> places;

    public PlacesDto(List<PlaceDto> places) {
        this.places = places;
    }

    public List<PlaceDto> getPlaces() {
        return places;
    }
}
