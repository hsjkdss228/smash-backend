package kr.megaptera.smash.controllers;

import kr.megaptera.smash.advices.AuthenticationErrorAdvice;
import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.AlreadyJoinedGame;
import kr.megaptera.smash.exceptions.GameIsFull;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.JoinGameService;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;
import kr.megaptera.smash.utils.JwtUtil;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@MockMvcEncoding
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AuthenticationErrorAdvice authenticationErrorAdvice;

    @MockBean
    private JoinGameService joinGameService;

    @MockBean
    private PatchRegisterToCanceledService patchRegisterToCanceledService;

    @MockBean
    private PatchRegisterToAcceptedService patchRegisterToAcceptedService;

    @MockBean
    private PatchRegisterToRejectedService patchRegisterToRejectedService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void registerToGame() throws Exception {
        Long userId = 1L;
        Long gameId = 1L;

        RegisterGameResultDto registerGameResultDto
            = new RegisterGameResultDto(gameId);

        given(joinGameService.joinGame(gameId, userId))
            .willReturn(registerGameResultDto);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"gameId\":1")
            ))
        ;
    }

    @Test
    void registerGameFailedWithUserNotFound() throws Exception {
        Long notExistedUserId = 139L;
        Long gameId = 1L;

        given(joinGameService.joinGame(gameId, notExistedUserId))
            .willThrow(UserNotFound.class);

        String token = jwtUtil.encode(notExistedUserId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void registerGameFailedWithGameNotFound() throws Exception {
        Long userId = 1L;
        Long wrongGameId = 222L;

        given(joinGameService.joinGame(wrongGameId, userId))
            .willThrow(GameNotFound.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/222")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void registerGameFailedWithAlreadyJoinedGame() throws Exception {
        Long userId = 1L;
        Long alreadyRegisteredGameId = 10L;

        given(joinGameService.joinGame(alreadyRegisteredGameId, userId))
            .willThrow(AlreadyJoinedGame.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/10")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;
    }

    @Test
    void registerGameFailedWithFullyParticipants() throws Exception {
        Long userId = 1L;
        Long gameId = 1L;

        given(joinGameService.joinGame(gameId, userId))
            .willThrow(GameIsFull.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;
    }

    @Test
    void cancelRegister() throws Exception {
        Long registerId = 4L;
        Long userId = 1L;

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "canceled"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToCanceledService).patchRegisterToCanceled(registerId, userId);
    }

    @Test
    void acceptRegister() throws Exception {
        Long registerId = 4L;
        Long userId = 1L;

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "accepted"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToAcceptedService).patchRegisterToAccepted(registerId);
    }

    @Test
    void acceptRegisterFailWithGameIsFull() throws Exception {
        Long registerId = 16L;
        Long userId = 1L;

        String token = jwtUtil.encode(userId);

        doThrow(GameIsFull.class).doNothing()
            .when(patchRegisterToAcceptedService).patchRegisterToAccepted(registerId);

        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/16")
                .header("Authorization", "Bearer " + token)
                .param("status", "accepted"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(patchRegisterToAcceptedService).patchRegisterToAccepted(registerId);
    }

    @Test
    void rejectRegister() throws Exception {
        Long registerId = 4L;
        Long userId = 1L;

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "rejected"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToRejectedService).patchRegisterToRejected(registerId);
    }
}
