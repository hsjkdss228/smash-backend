package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PlacesDto;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetPlacesService {
    private final PlaceRepository placeRepository;

    public GetPlacesService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public PlacesDto findAllPlaces() {
        List<Place> places = placeRepository.findAll();

        List<PlaceDto> placeDtos = places.stream()
            .map(Place::toPlaceDto)
            .toList();

        return new PlacesDto(placeDtos);
    }
}
