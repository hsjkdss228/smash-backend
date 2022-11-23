package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoginServiceTest {
    private UserRepository userRepository;
    private LoginService loginService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        loginService = new LoginService(userRepository, passwordEncoder);
    }

    @Test
    void verifyUser() {
        String identifier = "hsjkdss228";
        String password = "Password!1";
        User user = User.fake("찾아진 사용자", identifier);
        user.account().changePassword(password, passwordEncoder);
        given(userRepository.findByAccountIdentifier(identifier))
            .willReturn(Optional.of(user));

        Long userId = loginService.verifyUser(identifier, password);

        assertThat(userId).isEqualTo(userId);

        verify(userRepository).findByAccountIdentifier(identifier);
    }

    @Test
    void verifyUserFail() {
        String notExistingIdentifier = "abcdefgh123";
        String password = "Password!1";
        given(userRepository.findByAccountIdentifier(notExistingIdentifier))
            .willThrow(LoginFailed.class);

        assertThrows(LoginFailed.class, () -> {
            loginService.verifyUser(notExistingIdentifier, password);
        });
    }
}
