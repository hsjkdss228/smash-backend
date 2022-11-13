package kr.megaptera.smash.controllers;

import kr.megaptera.smash.services.DeleteGameMemberService;
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

import static org.mockito.Mockito.verify;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeleteGameMemberService deleteGameMemberService;

    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        token = jwtUtil.encode(userId);
    }

    @Test
    void deleteMember() throws Exception {
        userId = 1L;
        Long gameId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/members/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteGameMemberService).deleteGameMember(userId, gameId);
    }
}
