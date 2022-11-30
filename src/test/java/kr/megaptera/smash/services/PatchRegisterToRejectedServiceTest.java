package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class PatchRegisterToRejectedServiceTest {
    private PatchRegisterToRejectedService patchRegisterToRejectedService;

    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);

        patchRegisterToRejectedService
            = new PatchRegisterToRejectedService(registerRepository);
    }

    @Test
    void makeGameApplicantToMember() {
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

        patchRegisterToRejectedService.patchRegisterToRejected(registerId);

        verify(registerRepository).findById(registerId);
        verify(register).rejectRegister();
    }
}
