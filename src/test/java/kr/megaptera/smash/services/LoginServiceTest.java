package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoginServiceTest {
    private UserRepository userRepository;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        loginService = new LoginService(userRepository);
    }

    @Test
    void verifyUser() {
        Long userId = 1L;
        given(userRepository.findById(userId))
            .willReturn(Optional.of(User.fake("찾아진 사용자")));

        User user = loginService.verifyUser(userId);

        assertThat(user).isNotNull();
        assertThat(user.name().value()).isEqualTo("찾아진 사용자");

        verify(userRepository).findById(userId);
    }

    @Test
    void verifyUserFail() {
        Long notExistingUserId = 9999L;
        given(userRepository.findById(notExistingUserId))
            .willThrow(UserNotFound.class);

        assertThrows(LoginFailed.class, () -> {
            loginService.verifyUser(notExistingUserId);
        });
    }
}
