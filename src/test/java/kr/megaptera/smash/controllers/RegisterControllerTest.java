package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.services.PostRegisterGameService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(RegisterController.class)
class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRegisterGameService postRegisterGameService;

    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long gameId;
    private Long userId;

    @BeforeEach
    void setUp() {
        gameId = 1L;
        userId = 1L;
        token = jwtUtil.encode(userId);
    }

    @Test
    void registerToGame() throws Exception {
        RegisterGameResultDto registerGameResultDto
            = new RegisterGameResultDto(gameId);

        given(postRegisterGameService.registerGame(gameId, userId))
            .willReturn(registerGameResultDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"gameId\":1")
            ))
        ;
    }

    @Test
    void registerGameFailedWithGameNotFound() throws Exception {
        Long wrongGameId = 222L;
        given(postRegisterGameService.registerGame(wrongGameId, userId))
            .willThrow(new RegisterGameFailed(
                "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/222")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("100")
            ))
        ;
    }

    @Test
    void registerGameFailedWithAlreadyRegisteredGame() throws Exception {
        Long alreadyRegisteredGameId = 10L;
        given(postRegisterGameService.registerGame(alreadyRegisteredGameId, userId))
            .willThrow(new RegisterGameFailed(
                "이미 신청이 완료된 운동입니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/10")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("101")
            ))
        ;
    }

    @Test
    void registerGameFailedWithUserNotFound() throws Exception {
        Long notExistedUserId = 139L;
        token = jwtUtil.encode(notExistedUserId);
        given(postRegisterGameService.registerGame(gameId, notExistedUserId))
            .willThrow(new RegisterGameFailed(
                "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("102")
            ))
        ;
    }
}
