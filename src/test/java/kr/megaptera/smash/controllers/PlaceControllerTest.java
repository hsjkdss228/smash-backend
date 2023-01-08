package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PlaceSearchResultDto;
import kr.megaptera.smash.dtos.PlaceSearchResultsDto;
import kr.megaptera.smash.exceptions.PlaceNotFound;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.services.GetPlaceService;
import kr.megaptera.smash.services.SearchPlacesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(PlaceController.class)
class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPlaceService getPlaceService;

    @MockBean
    private SearchPlacesService searchPlacesService;

    @Test
    void place() throws Exception {
        Long placeId = 1L;
        PlaceDto placeDto = Place.fake("운동 장소 이름").toDto();
        given(getPlaceService.getTargetPlace(placeId))
            .willReturn(placeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/places/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("운동 장소 이름")
            ))
        ;
    }

    @Test
    void placeNotFound() throws Exception {
        Long wrongPlaceId = 2277L;
        given(getPlaceService.getTargetPlace(wrongPlaceId))
            .willThrow(PlaceNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/places/2277"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void foundPlaces() throws Exception {
        long generationCount = 3;
        List<PlaceSearchResultDto> placeSearchResultDtos = Place.fakes(generationCount)
            .stream()
            .map(Place::toSearchResultDto)
            .toList();
        PlaceSearchResultsDto placeSearchResultsDto
            = new PlaceSearchResultsDto(placeSearchResultDtos);

        String keyword = "이름";
        given(searchPlacesService.searchPlaces(keyword))
            .willReturn(placeSearchResultsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/places/search")
                .param("keyword", keyword))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("운동 장소 도로명주소 3")
            ))
        ;
    }
}
