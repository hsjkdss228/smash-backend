package kr.megaptera.smash.services;

import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
  private UserService userService;
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  void author() {
    Long postId = 1L;
    User user = new User(6L, "Author 1", 10.0);

    given(userRepository.findById(postId))
        .willReturn(Optional.of(user));

    User author = userService.user(postId);

    assertThat(author).isNotNull();
    assertThat(author.name()).isEqualTo("Author 1");

    verify(userRepository).findById(any(Long.class));
  }

  @Test
  void participant() {
    Long userId = 2L;
    User user = new User(1L, "Participant 1", 1.0);

    given(userRepository.findById(userId))
        .willReturn(Optional.of(user));

    User author = userService.user(userId);

    assertThat(author).isNotNull();
    assertThat(author.mannerScore()).isEqualTo(1.0);

    verify(userRepository).findById(any(Long.class));
  }
}
