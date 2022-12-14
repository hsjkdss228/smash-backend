package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.exercise.Exercise;
import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.game.GameDateTime;
import kr.megaptera.smash.models.game.GameTargetMemberCount;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class GetGameServiceTest {
    private GetGameService getGameService;

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        gameRepository = mock(GameRepository.class);
        registerRepository = mock(RegisterRepository.class);

        getGameService = new GetGameService(
            userRepository,
            gameRepository,
            registerRepository
        );
    }

    @Test
    void findTargetGame() {
        Long currentUserId = 10L;
        User user = User.fake(currentUserId);

        Long gameId = 1L;
        Long targetPostId = 1L;
        Long placeId = 1L;
        Game game = new Game(
            gameId,
            targetPostId,
            placeId,
            new Exercise("??????"),
            new GameDateTime(
                LocalDate.of(2022, 8, 22),
                LocalTime.of(19, 0),
                LocalTime.of(22, 0)
            ),
            new GameTargetMemberCount(10)
        );

        List<Register> registersAccepted = Register.fakesAccepted(8, 1L);

        given(userRepository.findById(user.id()))
            .willReturn(Optional.of(user));
        given(gameRepository.findByPostId(targetPostId))
            .willReturn(Optional.of(game));
        given(registerRepository.findAllByGameId(game.id()))
            .willReturn(registersAccepted);

        GameDetailDto gameDetailDto
            = getGameService.findTargetGame(currentUserId, targetPostId);

        assertThat(gameDetailDto).isNotNull();
        assertThat(gameDetailDto.getCurrentMemberCount()).isEqualTo(8);
        assertThat(gameDetailDto.getRegisterId()).isEqualTo(null);
        assertThat(gameDetailDto.getRegisterStatus()).isEqualTo("none");
    }

    @Test
    void findTargetGameWithNotLoggedIn() {
        Long currentUserId = null;

        Long gameId = 1L;
        Long targetPostId = 1L;
        Long placeId = 1L;
        Game game = new Game(
            gameId,
            targetPostId,
            placeId,
            new Exercise("??????"),
            new GameDateTime(
                LocalDate.of(2022, 8, 22),
                LocalTime.of(19, 0),
                LocalTime.of(22, 0)
            ),
            new GameTargetMemberCount(10)
        );

        List<Register> registersAccepted = Register.fakesAccepted(8, 1L);

        given(gameRepository.findByPostId(targetPostId))
            .willReturn(Optional.of(game));
        given(registerRepository.findAllByGameId(game.id()))
            .willReturn(registersAccepted);

        GameDetailDto gameDetailDto
            = getGameService.findTargetGame(currentUserId, targetPostId);

        assertThat(gameDetailDto).isNotNull();
        assertThat(gameDetailDto.getCurrentMemberCount()).isEqualTo(8);
        assertThat(gameDetailDto.getRegisterId()).isEqualTo(null);
        assertThat(gameDetailDto.getRegisterStatus()).isEqualTo("none");

        verify(userRepository, never()).findById(any(Long.class));
    }

    @Test
    void findTargetGameWithUserNotFound() {
        Long wrongUserId = 996L;
        Long targetPostId = 1L;

        given(userRepository.findById(wrongUserId))
            .willThrow(UserNotFound.class);

        assertThrows(UserNotFound.class, () -> {
            getGameService.findTargetGame(wrongUserId, targetPostId);
        });

        verify(userRepository).findById(wrongUserId);
    }

    @Test
    void findTargetGameWithGameNotFound() {
        Long currentUserId = null;
        Long targetPostId = 2432L;

        given(gameRepository.findByPostId(targetPostId))
            .willThrow(GameNotFound.class);

        assertThrows(GameNotFound.class, () -> {
            getGameService.findTargetGame(currentUserId, targetPostId);
        });

        verify(gameRepository).findByPostId(targetPostId);
    }
}
