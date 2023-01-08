package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceSearchResultDto;
import kr.megaptera.smash.dtos.PlaceSearchResultsDto;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.specifications.PlaceSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchPlacesService {
    private final PlaceRepository placeRepository;

    public SearchPlacesService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public PlaceSearchResultsDto searchPlaces(String keyword) {
        Specification<Place> specification
            = Specification.where(PlaceSpecification.likeName(keyword));

        List<PlaceSearchResultDto> placeSearchResultDtos
            = placeRepository.findAll(specification)
            .stream()
            .filter(Place::registered)
            .map(Place::toSearchResultDto)
            .toList();

        return new PlaceSearchResultsDto(placeSearchResultDtos);
    }
}
