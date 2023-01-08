package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PlacesDto;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.services.GetPlacesService;
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
@WebMvcTest(AdminPlaceController.class)
class AdminPlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPlacesService getPlacesService;

    @Test
    void places() throws Exception {
        long generationCount = 3;
        List<PlaceDto> placeDtos = Place.fakes(generationCount)
            .stream().map(Place::toDto)
            .toList();
        PlacesDto placesDto = new PlacesDto(placeDtos);

        given(getPlacesService.findAllPlaces())
            .willReturn(placesDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/places"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"address\":\"운동 장소 도로명주소 1\"")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"contactNumber\":\"010-1234-5673\"")
            ))
        ;
    }
}
