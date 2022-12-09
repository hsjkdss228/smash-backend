package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PlacesDto;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetPlacesServiceTest {
    private GetPlacesService getPlacesService;

    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        placeRepository = mock(PlaceRepository.class);

        getPlacesService = new GetPlacesService(placeRepository);
    }

    @Test
    void findAllPlaces() {
        long generationCount = 3;
        List<Place> places = Place.fakes(generationCount);

        given(placeRepository.findAll()).willReturn(places);

        PlacesDto placesDto = getPlacesService.findAllPlaces();

        assertThat(placesDto).isNotNull();
        List<PlaceDto> placeDtos = placesDto.getPlaces();
        assertThat(placeDtos).hasSize(3);
        assertThat(placeDtos.get(0).getName()).isEqualTo("운동 장소 이름 1");
        assertThat(placeDtos.get(2).getAddress()).isEqualTo("운동 장소 도로명주소 3");
    }
}
