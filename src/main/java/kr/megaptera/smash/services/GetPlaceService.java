package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.exceptions.PlaceNotFound;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetPlaceService {
    private final PlaceRepository placeRepository;

    public GetPlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public PlaceDto getTargetPlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new PlaceNotFound(placeId));

        return place.toDto();
    }
}
