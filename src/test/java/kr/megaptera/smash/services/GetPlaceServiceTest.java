package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetPlaceServiceTest {
    private GetPlaceService getPlaceService;

    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        placeRepository = mock(PlaceRepository.class);

        getPlaceService = new GetPlaceService(placeRepository);
    }

    @Test
    void findTargetPlace() {
        Place place = Place.fake("장소 이름");
        Long targetPlaceId = place.id();

        given(placeRepository.findById(targetPlaceId))
            .willReturn(Optional.of(place));

        PlaceDto placeDto = getPlaceService.getTargetPlace(targetPlaceId);

        assertThat(placeDto).isNotNull();
        assertThat(placeDto.getName()).isEqualTo("장소 이름");
    }
}
