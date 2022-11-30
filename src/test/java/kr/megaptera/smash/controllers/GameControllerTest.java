package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.services.GetGameService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetGameService getGameService;

    private GameDetailDto gameDetailDto;

    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long userId = 1L;
    private Long targetPostId = 1L;

    @BeforeEach
    void setUp() {
        token = jwtUtil.encode(userId);

        Game game = Game.fake("스케이트", "Mokdong Ice Rink");
        List<Register> members = Register.fakesAccepted(5, game.id());

        Long registerId = -1L;
        String registerStatus = "none";

        gameDetailDto = new GameDetailDto(
            game.id(),
            game.exercise().name(),
            game.dateTime().joinDateAndTime(),
            game.place().name(),
            members.size(),
            game.targetMemberCount().value(),
            registerId,
            registerStatus
        );
    }

    @Test
    void game() throws Exception {
        given(getGameService.findTargetGame(userId, targetPostId))
            .willReturn(gameDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/posts/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"place\":\"Mokdong Ice Rink\"")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"registerId\":-1")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"registerStatus\":\"none\"")
            ))
        ;
    }
}
