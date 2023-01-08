package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceSearchResultDto;
import kr.megaptera.smash.dtos.PlaceSearchResultsDto;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.models.place.PlaceRegistrationStatus;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SearchPlacesServiceTest {
    private SearchPlacesService searchPlacesService;

    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        placeRepository = mock(PlaceRepository.class);

        searchPlacesService = new SearchPlacesService(placeRepository);
    }

    @Test
    void searchPlaces() {
        long generationCount = 3;
        List<Place> places = Place.fakes(generationCount);
        places.add(
            Place.fake("미등록된 장소", PlaceRegistrationStatus.UNREGISTERED));

        given(placeRepository.findAll(any(Specification.class)))
            .willReturn(places);

        String keyword = "이름";
        PlaceSearchResultsDto placeSearchResultsDto
            = searchPlacesService.searchPlaces(keyword);

        assertThat(placeSearchResultsDto).isNotNull();
        List<PlaceSearchResultDto> placeSearchResultDtos
            = placeSearchResultsDto.getSearchedPlaces();
        assertThat(placeSearchResultDtos).hasSize(3);
        placeSearchResultDtos.forEach(placeSearchResultDto -> {
            assertThat(placeSearchResultDto.getName())
                .contains("이름");
        });
    }
}
