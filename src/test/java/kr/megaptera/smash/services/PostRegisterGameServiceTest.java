package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDate;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserGender;
import kr.megaptera.smash.models.UserName;
import kr.megaptera.smash.models.UserPhoneNumber;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PostRegisterGameServiceTest {
    private PostRegisterGameService postRegisterGameService;

    private GameRepository gameRepository;
    private RegisterRepository registerRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        registerRepository = mock(RegisterRepository.class);
        userRepository = mock(UserRepository.class);

        postRegisterGameService = new PostRegisterGameService(
            gameRepository,
            registerRepository,
            userRepository
        );
    }

    @Test
    void registerGame() {
        Long gameId = 1L;
        Game game = new Game(
            gameId,
            1L,
            new Exercise("운동 종류"),
            new GameDate("운동 날짜"),
            new Place("운동 장소"),
            new GameTargetMemberCount(5)
        );
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        List<Register> members = List.of(
            new Register(
                1L,
                2L,
                gameId,
                new RegisterStatus(RegisterStatus.ACCEPTED)),
            new Register(
                2L,
                3L,
                gameId,
                new RegisterStatus(RegisterStatus.ACCEPTED))
        );
        given(registerRepository.findByGameId(gameId)).willReturn(members);

        Long userId = 1L;
        User user = new User(
            userId,
            new UserName("사용자명"),
            new UserGender("남성"),
            new UserPhoneNumber("010-2222-2222")
        );
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Register registeredMember = new Register(
            3L,
            userId,
            gameId,
            new RegisterStatus(RegisterStatus.ACCEPTED)
        );
        given(registerRepository.save(any(Register.class)))
            .willReturn(registeredMember);

        RegisterGameResultDto registerGameResultDto
            = postRegisterGameService.registerGame(gameId, userId);

        assertThat(registerGameResultDto).isNotNull();
        assertThat(registerGameResultDto.getGameId()).isEqualTo(gameId);

        verify(gameRepository).findById(gameId);
        verify(registerRepository).findByGameId(gameId);
        verify(registerRepository).save(any(Register.class));
    }

    @Test
    void registerGameWithGameNotFound() {
        Long wrongGameId = 100L;
        Long userId = 1L;
        given(gameRepository.findById(wrongGameId))
            .willThrow(RegisterGameFailed.class);

        assertThrows(RegisterGameFailed.class, () -> {
            postRegisterGameService.registerGame(wrongGameId, userId);
        });

        verify(gameRepository).findById(wrongGameId);
        verify(registerRepository, never()).findByGameId(any(Long.class));
        verify(userRepository, never()).findById(any(Long.class));
        verify(registerRepository, never()).save(any(Register.class));
    }

    @Test
    void registerGameWithAlreadyRegisteredGame() {
        Long alreadyRegisteredGameId = 100L;
        Game game = new Game(
            alreadyRegisteredGameId,
            1L,
            new Exercise("운동 종류"),
            new GameDate("운동 날짜"),
            new Place("운동 장소"),
            new GameTargetMemberCount(5)
        );
        given(gameRepository.findById(alreadyRegisteredGameId))
            .willReturn(Optional.of(game));

        Long userId = 1L;
        List<Register> members = List.of(
            new Register(
                1L,
                userId,
                alreadyRegisteredGameId,
                new RegisterStatus(RegisterStatus.ACCEPTED))
        );
        given(registerRepository.findByGameId(alreadyRegisteredGameId))
            .willReturn(members);

        assertThrows(RegisterGameFailed.class, () -> {
            postRegisterGameService.registerGame(alreadyRegisteredGameId, userId);
        });

        verify(gameRepository).findById(alreadyRegisteredGameId);
        verify(registerRepository).findByGameId(alreadyRegisteredGameId);
        verify(userRepository, never()).findById(any(Long.class));
        verify(registerRepository, never()).save(any(Register.class));
    }

    @Test
    void registerGameWithUserNotFound() {
        Long gameId = 1L;
        Game game = new Game(
            gameId,
            1L,
            new Exercise("운동 종류"),
            new GameDate("운동 날짜"),
            new Place("운동 장소"),
            new GameTargetMemberCount(5)
        );
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        Long notExistedUserId = 113L;
        List<Register> members = List.of(
            new Register(
                1L,
                notExistedUserId,
                gameId,
                new RegisterStatus(RegisterStatus.ACCEPTED))
        );
        given(registerRepository.findByGameId(gameId))
            .willReturn(members);
        given(userRepository.findById(notExistedUserId))
            .willThrow(RegisterGameFailed.class);

        assertThrows(RegisterGameFailed.class, () -> {
            postRegisterGameService.registerGame(gameId, notExistedUserId);
        });

        verify(gameRepository).findById(gameId);
        verify(registerRepository).findByGameId(gameId);
        verify(userRepository, never()).findById(notExistedUserId);
        verify(registerRepository, never()).save(any(Register.class));
    }
}
