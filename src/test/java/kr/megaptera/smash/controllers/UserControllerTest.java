package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.SignUpResultDto;
import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetUserNameService;
import kr.megaptera.smash.services.SignUpService;
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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUserNameService getUserNameService;

    @MockBean
    private SignUpService signUpService;

    @SpyBean
    private JwtUtil jwtUtil;

    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String gender;
    private String phoneNumber;

    @BeforeEach()
    void setUp() {
        name = "황인우";
        username = "hsjkdss228";
        password = "Password!1";
        confirmPassword = "Password!1";
        gender = "남성";
        phoneNumber = "01068772291";
    }

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

    @Test
    void signUp() throws Exception {
        SignUpResultDto signUpResultDto = new SignUpResultDto(name);
        given(signUpService.createUser(
            name,
            username,
            password,
            confirmPassword,
            gender,
            phoneNumber
        )).willReturn(signUpResultDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"name\":\"" + name + "\"," +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + password + "\"," +
                    "\"confirmPassword\":\"" + confirmPassword + "\"," +
                    "\"gender\":\"" + gender + "\"," +
                    "\"phoneNumber\":\"" + phoneNumber + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    void performSignUpFailed(
        String name,
        String username,
        String password,
        String confirmPassword,
        String gender,
        String phoneNumber
    ) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"name\":\"" + name + "\"," +
                    "\"username\":\"" + username + "\"," +
                    "\"password\":\"" + password + "\"," +
                    "\"confirmPassword\":\"" + confirmPassword + "\"," +
                    "\"gender\":\"" + gender + "\"," +
                    "\"phoneNumber\":\"" + phoneNumber + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void signUpWithWrongFormat() {
        List<String> namesWithWrongFormat = List.of(
            "",
            "사",
            "열글자넘는사용자이름사",
            "english이름",
            "1숫자있는이름"
        );
        namesWithWrongFormat.forEach(wrongName -> {
            try {
                performSignUpFailed(
                    wrongName,
                    username,
                    password,
                    confirmPassword,
                    gender,
                    phoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void signUpWithInvalidNameFormat() {
        List<String> namesWithWrongFormat = List.of(
            "",
            "사",
            "열글자넘는사용자이름사",
            "english이름",
            "1숫자있는이름"
        );
        namesWithWrongFormat.forEach(wrongName -> {
            try {
                performSignUpFailed(
                    wrongName,
                    username,
                    password,
                    confirmPassword,
                    gender,
                    phoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void signUpWithWrongNames() {
        List<String> wrongNames = List.of(
            "",
            "사",
            "열글자넘는사용자이름사",
            "english이름",
            "1숫자있는이름"
        );
        wrongNames.forEach(wrongName -> {
            try {
                performSignUpFailed(
                    wrongName,
                    username,
                    password,
                    confirmPassword,
                    gender,
                    phoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void signUpWithWrongUsernames() {
        List<String> wrongUsernames = List.of(
            "",
            "hsjkdss",
            "12345678",
            "hs1",
            "hsjkdss228hsjkdss228"
        );
        wrongUsernames.forEach(wrongUsername -> {
            try {
                performSignUpFailed(
                    name,
                    wrongUsername,
                    password,
                    confirmPassword,
                    gender,
                    phoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void signUpWithWrongPasswords() {
        List<String> wrongPasswords = List.of(
            "",
            "password",
            "password12",
            "PASSword",
            "Password!",
            "!@!@#12123",
            "Pa!2"
        );
        wrongPasswords.forEach(wrongPassword -> {
            try {
                performSignUpFailed(
                    name,
                    username,
                    wrongPassword,
                    confirmPassword,
                    gender,
                    phoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void signUpWithWrongPhoneNumbers() {
        List<String> wrongPhoneNumbers = List.of(
            "",
            "0100000",
            "010-0000-0000",
            "010101010101010101010"
            );
        wrongPhoneNumbers.forEach(wrongPhoneNumber -> {
            try {
                performSignUpFailed(
                    name,
                    username,
                    password,
                    confirmPassword,
                    gender,
                    wrongPhoneNumber
                );
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
