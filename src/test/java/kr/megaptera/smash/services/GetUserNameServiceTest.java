package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetUserNameServiceTest {
    private GetUserNameService getUserNameService;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        getUserNameService = new GetUserNameService(userRepository);
    }

    @Test
    void getUserName() {
        Long userId = 1L;
        User user = User.fake("치코리타", "chikorita");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserNameDto userNameDto = getUserNameService.getUserName(userId);

        assertThat(userNameDto).isNotNull();
        assertThat(userNameDto.getName()).isEqualTo("치코리타");

        verify(userRepository).findById(userId);
    }

    @Test
    void getUserNameWithUserNotFound() {
        Long wrongUserId = 2222L;

        given(userRepository.findById(wrongUserId))
            .willThrow(UserNotFound.class);

        assertThrows(UserNotFound.class, () -> {
            getUserNameService.getUserName(wrongUserId);
        });
    }
}
