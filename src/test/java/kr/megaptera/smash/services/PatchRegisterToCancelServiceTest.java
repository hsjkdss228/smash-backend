package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PatchRegisterToCancelServiceTest {
    private PatchRegisterToCancelService patchRegisterToCancelService;

    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);

        patchRegisterToCancelService
            = new PatchRegisterToCancelService(registerRepository);
    }

    @Test
    void deleteGameMember() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register = mock(Register.class);

        given(registerRepository.findByGameIdAndUserId(gameId, userId))
            .willReturn(Optional.of(register));

        patchRegisterToCancelService.patchRegisterToCancel(userId, gameId);

        verify(registerRepository).findByGameIdAndUserId(gameId, userId);
        verify(register).cancelRegister();
    }
}
