package kr.megaptera.smash.services;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.repositories.RegisterRepository;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class PatchRegisterToCanceledServiceTest {
    private PatchRegisterToCanceledService patchRegisterToCanceledService;

    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);

        patchRegisterToCanceledService
                = new PatchRegisterToCanceledService(registerRepository);
    }

    @Test
    void deleteGameApplicant() {
        Long registerId = 12L;
        Long userId = 1L;
        Register register = spy(new Register(
                1L,
                userId,
                1L,
                RegisterStatus.processing()
        ));

        given(registerRepository.findById(registerId))
                .willReturn(Optional.of(register));

        patchRegisterToCanceledService.patchRegisterToCanceled(registerId, userId);

        verify(registerRepository).findById(registerId);
        verify(register).cancelRegister();
    }

    @Test
    void deleteGameMember() {
        Long registerId = 8L;
        Long userId = 1L;
        Register register = spy(new Register(
                1L,
                userId,
                1L,
                RegisterStatus.accepted()
        ));

        given(registerRepository.findById(registerId))
                .willReturn(Optional.of(register));

        patchRegisterToCanceledService.patchRegisterToCanceled(registerId, userId);

        verify(registerRepository).findById(registerId);
        verify(register).cancelRegister();
    }
}
