package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
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

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        token = jwtUtil.encode(userId);
    }

    @Test
    void registerToGame() throws Exception {
        Long gameId = 1L;
        Long userId = 1L;
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
}
