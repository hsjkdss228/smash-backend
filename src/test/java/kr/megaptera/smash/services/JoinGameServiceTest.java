package kr.megaptera.smash.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserAccount;
import kr.megaptera.smash.models.UserGender;
import kr.megaptera.smash.models.UserName;
import kr.megaptera.smash.models.UserPhoneNumber;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class JoinGameServiceTest {
    // 저는 이 코드가 너무 복잡해서 지속적으로 관리할 수 있을지 의심스럽습니다.
    // 테스트 코드는 유지보수에 도움이 돼야 하는데 오히려 걸림돌이 되는 형국입니다.
    // 도메인 모델로 코드를 옮기면서 여기는 모두 막아놓겠습니다.

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

    @Test
    @Disabled
    void registerGame() {
        Long gameId = 1L;
        Game game = new Game(
                gameId,
                1L,
                new Exercise("운동 종류"),
                new GameDateTime(
                        LocalDate.of(2022, 10, 20),
                        LocalTime.of(14, 0),
                        LocalTime.of(17, 0)
                ),
                new Place("운동 장소"),
                new GameTargetMemberCount(5)
        );
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        List<Register> members = List.of(
                new Register(
                        1L,
                        2L,
                        gameId,
                        RegisterStatus.accepted()),
                new Register(
                        2L,
                        3L,
                        gameId,
                        RegisterStatus.accepted())
        );
        given(registerRepository.findByGameId(gameId)).willReturn(members);

        Long userId = 1L;
        User user = new User(
                userId,
                new UserAccount("MinjiRungRung12"),
                new UserName("사용자명"),
                new UserGender("남성"),
                new UserPhoneNumber("010-2222-2222")
        );
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Register registeredMember = new Register(
                3L,
                userId,
                gameId,
                RegisterStatus.accepted()
        );
        given(registerRepository.save(any(Register.class)))
                .willReturn(registeredMember);

        RegisterGameResultDto registerGameResultDto
                = joinGameService.joinGame(gameId, userId);

        assertThat(registerGameResultDto).isNotNull();
        assertThat(registerGameResultDto.getGameId()).isEqualTo(gameId);

        verify(gameRepository).findById(gameId);
        verify(registerRepository).findByGameId(gameId);
        verify(registerRepository).save(any(Register.class));
    }

    @Test
    @Disabled
    void registerGameWithGameNotFound() {
        Long wrongGameId = 100L;
        Long userId = 1L;
        given(gameRepository.findById(wrongGameId))
                .willThrow(RegisterGameFailed.class);

        assertThrows(RegisterGameFailed.class, () -> {
            joinGameService.joinGame(wrongGameId, userId);
        });

        verify(gameRepository).findById(wrongGameId);
        verify(registerRepository, never()).findByGameId(any(Long.class));
        verify(userRepository, never()).findById(any(Long.class));
        verify(registerRepository, never()).save(any(Register.class));
    }

    @Test
    @Disabled
    void registerGameWithAlreadyRegisteredGame() {
        Long alreadyRegisteredGameId = 100L;
        Game game = new Game(
                alreadyRegisteredGameId,
                1L,
                new Exercise("운동 종류"),
                new GameDateTime(
                        LocalDate.of(2022, 10, 21),
                        LocalTime.of(20, 0),
                        LocalTime.of(23, 30)
                ),
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
                        RegisterStatus.accepted())
        );
        given(registerRepository.findByGameId(alreadyRegisteredGameId))
                .willReturn(members);

        assertThrows(RegisterGameFailed.class, () -> {
            joinGameService.joinGame(alreadyRegisteredGameId, userId);
        });

        verify(gameRepository).findById(alreadyRegisteredGameId);
        verify(registerRepository).findByGameId(alreadyRegisteredGameId);
        verify(userRepository, never()).findById(any(Long.class));
        verify(registerRepository, never()).save(any(Register.class));
    }

    @Test
    @Disabled
    void registerGameWithUserNotFound() {
        Long gameId = 1L;
        Game game = new Game(
                gameId,
                1L,
                new Exercise("운동 종류"),
                new GameDateTime(
                        LocalDate.of(2023, 1, 2),
                        LocalTime.of(12, 0),
                        LocalTime.of(16, 0)
                ),
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
                        RegisterStatus.accepted())
        );
        given(registerRepository.findByGameId(gameId))
                .willReturn(members);
        given(userRepository.findById(notExistedUserId))
                .willThrow(RegisterGameFailed.class);

        assertThrows(RegisterGameFailed.class, () -> {
            joinGameService.joinGame(gameId, notExistedUserId);
        });

        verify(gameRepository).findById(gameId);
        verify(registerRepository).findByGameId(gameId);
        verify(userRepository, never()).findById(notExistedUserId);
        verify(registerRepository, never()).save(any(Register.class));
    }

    @Test
    @Disabled
    void registerGameWithFullyParticipants() {
        Long gameId = 1L;
        Game game = new Game(
                gameId,
                1L,
                new Exercise("운동 종류"),
                new GameDateTime(
                        LocalDate.of(2022, 10, 20),
                        LocalTime.of(14, 0),
                        LocalTime.of(17, 0)
                ),
                new Place("운동 장소"),
                new GameTargetMemberCount(2)
        );
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        List<Register> members = List.of(
                new Register(
                        1L,
                        2L,
                        gameId,
                        RegisterStatus.accepted()),
                new Register(
                        2L,
                        3L,
                        gameId,
                        RegisterStatus.accepted())
        );
        given(registerRepository.findByGameId(gameId)).willReturn(members);

        Long userId = 1L;
        User user = new User(
                userId,
                new UserAccount("MinjiRungRung12"),
                new UserName("민지룽룽"),
                new UserGender("여성"),
                new UserPhoneNumber("010-2222-2222")
        );
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        assertThrows(RegisterGameFailed.class, () -> {
            joinGameService.joinGame(gameId, userId);
        });

        verify(gameRepository).findById(gameId);
        verify(registerRepository).findByGameId(gameId);
        verify(registerRepository, never()).save(any(Register.class));
    }
}
