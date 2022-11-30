package kr.megaptera.smash.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.services.LoginService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockMvcEncoding
@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @SpyBean
    private JwtUtil jwtUtil;

    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        username = "hsjkdss228";
        password = "Password!1";
    }

    @Test
    void login() throws Exception {
        Long userId = 1L;
        given(loginService.verifyUser(username, password))
            .willReturn(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + password + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isCreated())
        ;

        verify(jwtUtil).encode(1L);
    }

    @Test
    void loginWithEmptyIdentifier() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + "\"," +
                    "\"password\":\"" + password + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("아이디를 입력해주세요.")
            ))
        ;

        verify(loginService, never()).verifyUser(any(), any());
    }

    @Test
    void loginWithNotExistingIdentifier() throws Exception {
        String notExistingIdentifier = "WrongIdentifier1234";
        given(loginService.verifyUser(notExistingIdentifier, password))
            .willThrow(new LoginFailed("존재하지 않는 아이디입니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + notExistingIdentifier + "\"," +
                    "\"password\":\"" + password + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("존재하지 않는 아이디입니다.")
            ))
        ;

        verify(loginService).verifyUser(notExistingIdentifier, password);
    }

    @Test
    void loginWithEmptyPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("비밀번호를 입력해주세요.")
            ))
        ;

        verify(loginService, never()).verifyUser(any(), any());
    }

    @Test
    void loginWithNotMatchingPassword() throws Exception {
        String notMatchingPassword = "WrongPassword!1";
        given(loginService.verifyUser(username, notMatchingPassword))
            .willThrow(new LoginFailed("비밀번호가 일치하지 않습니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + notMatchingPassword + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("비밀번호가 일치하지 않습니다.")
            ))
        ;

        verify(loginService).verifyUser(username, notMatchingPassword);
    }

    @Test
    void loginWithEncodingError() throws Exception {
        Long userId = 1L;
        given(loginService.verifyUser(username, password))
            .willReturn(userId);
        given(jwtUtil.encode(userId))
            .willThrow(JWTDecodeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + password + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("인가 과정에서 문제가 발생했습니다.")
            ))
        ;

        verify(loginService).verifyUser(username, password);
        verify(jwtUtil).encode(1L);
    }
}
