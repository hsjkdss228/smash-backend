package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.services.GetPlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(PlaceController.class)
class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPlaceService getPlaceService;

    @Test
    void place() throws Exception {
        Long placeId = 1L;
        PlaceDto placeDto = Place.fake("운동 장소 이름").toPlaceDto();
        given(getPlaceService.getTargetPlace(placeId))
            .willReturn(placeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/places/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("운동 장소 이름")
            ))
        ;
    }
}
