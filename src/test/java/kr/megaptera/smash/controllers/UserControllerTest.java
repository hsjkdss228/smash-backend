package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetUserNameService;
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

@MockMvcEncoding
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUserNameService getUserNameService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void userName() throws Exception {
        Long userId = 1L;
        String token = jwtUtil.encode(userId);

        UserNameDto userNameDto = new UserNameDto("황인우");
        given(getUserNameService.getUserName(userId))
            .willReturn(userNameDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("황인우")
            ));
    }

    @Test
    void userNameWithUserNotFound() throws Exception {
        Long userId = 1L;
        String token = jwtUtil.encode(userId);

        given(getUserNameService.getUserName(userId))
            .willThrow(UserNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
