package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.GameIsFull;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.NoticeRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class PatchRegisterToAcceptedServiceTest {
    private PatchRegisterToAcceptedService patchRegisterToAcceptedService;

    private RegisterRepository registerRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);
        userRepository = mock(UserRepository.class);
        gameRepository = mock(GameRepository.class);
        noticeRepository = mock(NoticeRepository.class);

        patchRegisterToAcceptedService = new PatchRegisterToAcceptedService(
            registerRepository,
            userRepository,
            gameRepository,
            noticeRepository);
    }

    @Test
    void acceptRegister() {
        Long registerId = 12L;
        Long userId = 1L;
        Long gameId = 1L;
        Long postId = 1L;

        User user = User.fake(userId);
        Game game = Game.fake(gameId, postId, new GameTargetMemberCount(3));
        Register register = spy(Register.fakeProcessing(userId, gameId));
        List<Register> registers = List.of(
            Register.fakeProcessing(userId, gameId),
            Register.fakeAccepted(2L, gameId),
            Register.fakeAccepted(3L, gameId)
        );

        given(registerRepository.findById(registerId))
            .willReturn(Optional.of(register));
        given(gameRepository.findById(register.gameId()))
            .willReturn(Optional.of(game));
        given(registerRepository.findAllByGameId(game.id()))
            .willReturn(registers);
        given(userRepository.findById(register.userId()))
            .willReturn(Optional.of(user));

        patchRegisterToAcceptedService.patchRegisterToAccepted(registerId);

        verify(registerRepository).findById(registerId);
        verify(register).acceptRegister();
        verify(register).createAcceptNotice(user, game);
        verify(noticeRepository).save(any(Notice.class));
    }

    @Test
    void acceptRegisterFailWithGameIsFull() {
        Long registerId = 6L;
        Long userId = 1L;
        Long gameId = 1L;
        Long postId = 1L;

        User user = User.fake(userId);
        Game game = Game.fake(gameId, postId, new GameTargetMemberCount(3));
        Register register = spy(Register.fakeProcessing(userId, gameId));
        List<Register> registers = List.of(
            Register.fakeProcessing(userId, gameId),
            Register.fakeAccepted(2L, gameId),
            Register.fakeAccepted(3L, gameId),
            Register.fakeAccepted(4L, gameId)
        );

        given(registerRepository.findById(registerId))
            .willReturn(Optional.of(register));
        given(gameRepository.findById(register.gameId()))
            .willReturn(Optional.of(game));
        given(registerRepository.findAllByGameId(game.id()))
            .willReturn(registers);

        assertThrows(GameIsFull.class, () -> {
            patchRegisterToAcceptedService.patchRegisterToAccepted(registerId);
        });
    }
}
