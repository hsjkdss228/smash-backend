package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.IsNotRegisterOfCurrentUser;
import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.models.register.RegisterStatus;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class CancelRegisterServiceTest {
    private CancelRegisterService cancelRegisterService;

    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);

        cancelRegisterService
            = new CancelRegisterService(registerRepository);
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

        cancelRegisterService.cancelRegister(registerId, userId);

        verify(registerRepository).findById(registerId);
        verify(register).cancel();
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

        cancelRegisterService.cancelRegister(registerId, userId);

        verify(registerRepository).findById(registerId);
        verify(register).cancel();
    }

    @Test
    void cancelRegisterWithRegisterNotFound() {
        Long wrongRegisterId = 2222L;
        Long userId = 1L;

        given(registerRepository.findById(wrongRegisterId))
            .willThrow(RegisterNotFound.class);

        assertThrows(RegisterNotFound.class, () -> {
            cancelRegisterService.cancelRegister(wrongRegisterId, userId);
        });

        verify(registerRepository).findById(wrongRegisterId);
    }

    @Test
    void isNotRegisterOfCurrentUser() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register = spy(Register.fakeProcessing(userId, gameId));

        Long registerId = register.id();
        given(registerRepository.findById(registerId))
            .willReturn(Optional.of(register));

        Long anotherUserId = 9876L;
        assertThrows(IsNotRegisterOfCurrentUser.class, () -> {
            cancelRegisterService.cancelRegister(registerId, anotherUserId);
        });

        verify(registerRepository).findById(registerId);
        verify(register).match(anotherUserId);
    }
}
