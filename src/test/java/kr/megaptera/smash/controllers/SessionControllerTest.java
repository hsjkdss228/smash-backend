package kr.megaptera.smash.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.LoginService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void login() throws Exception {
        Long userId = 1L;
        given(loginService.verifyUser(userId)).willReturn(User.fake("사용자"));

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"userId\":" + userId +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isCreated())
        ;

        verify(jwtUtil).encode(1L);
    }

    @Test
    void loginWithEmptyUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"userId\":\"\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("200")
            ))
        ;
    }

    @Test
    void loginWithNotExistingUserId() throws Exception {
        Long notExistingUserId = 9999L;
        given(loginService.verifyUser(notExistingUserId))
            .willThrow(new LoginFailed("존재하지 않는 User Id 입니다. (201)"));

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"userId\":" + notExistingUserId +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("201")
            ))
        ;
    }

    @Test
    void loginWithEncodingError() throws Exception {
        Long userId = 1L;
        given(loginService.verifyUser(userId)).willReturn(User.fake("사용자"));
        given(jwtUtil.encode(userId))
            .willThrow(JWTDecodeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"userId\":" + userId +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("202")
            ))
        ;

        verify(jwtUtil).encode(1L);
    }
}
