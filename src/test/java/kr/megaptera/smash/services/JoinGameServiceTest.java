package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JoinGameServiceTest {
    private JoinGameService joinGameService;

    private GameRepository gameRepository;
    private RegisterRepository registerRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        registerRepository = mock(RegisterRepository.class);
        userRepository = mock(UserRepository.class);

        joinGameService = new JoinGameService(
            gameRepository,
            registerRepository,
            userRepository
        );
    }

    // TODO: Service에서 발생하는 에러 검증하는 테스트 코드 작성
    //   UserNotFound, GameNotFound

    @Test
    void joinGame() {
        Long userId = 4L;
        User user = User.fake(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Long gameId = 1L;
        Game game = Game.fake(new GameTargetMemberCount(3));
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.accepted()),
            Register.fake(2L, game.id(), RegisterStatus.canceled()),
            Register.fake(3L, game.id(), RegisterStatus.rejected())
        );
        given(registerRepository.findByGameId(game.id()))
            .willReturn(registers);

        RegisterGameResultDto registerGameResultDto
            = joinGameService.joinGame(gameId, userId);

        assertThat(registerGameResultDto.getGameId()).isEqualTo(1L);

        verify(registerRepository).save(any(Register.class));
    }

    @Test
    void joinGameWithInvalidUser() {
        Long invalidUserId = 5000L;
        given(userRepository.findById(invalidUserId))
            .willThrow(UserNotFound.class);

        Long gameId = 1L;

        assertThrows(UserNotFound.class, () -> {
            joinGameService.joinGame(gameId, invalidUserId);
        });
    }

    @Test
    void joinGameWithInvalidGame() {
        Long userId = 1L;
        User user = User.fake(userId);
        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        Long invalidGameId = 9999L;
        given(gameRepository.findById(invalidGameId))
            .willThrow(GameNotFound.class);

        assertThrows(GameNotFound.class, () -> {
            joinGameService.joinGame(invalidGameId, userId);
        });
    }
}
