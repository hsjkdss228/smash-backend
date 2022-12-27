package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegistersAcceptedDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetAcceptedRegisterServiceTest {
    private GetAcceptedRegisterService getAcceptedRegisterService;

    private RegisterRepository registerRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);
        userRepository = mock(UserRepository.class);
        getAcceptedRegisterService
            = new GetAcceptedRegisterService(registerRepository,userRepository);
    }

    @Test
    void findMembers() {
        Long targetGameId = 1L;
        List<Register> members = Register.fakesAccepted(2, targetGameId);
        List<User> users = User.fakes(2);

        given(registerRepository.findAllByGameId(targetGameId))
            .willReturn(members);
        given(userRepository.findById(members.get(0).userId()))
            .willReturn(Optional.of(users.get(0)));
        given(userRepository.findById(members.get(1).userId()))
            .willReturn(Optional.of(users.get(1)));

        RegistersAcceptedDto registersAcceptedDto
            = getAcceptedRegisterService.findAcceptedRegisters(targetGameId);

        assertThat(registersAcceptedDto).isNotNull();
        assertThat(registersAcceptedDto.getMembers().size()).isEqualTo(2);
    }

    @Test
    void findMembersWithUserNotFound() {
        Long targetGameId = 1L;
        List<Register> members = Register.fakesAccepted(2, targetGameId);
        User user = User.fake("사용자 이름", "username12");

        given(registerRepository.findAllByGameId(targetGameId))
            .willReturn(members);
        given(userRepository.findById(members.get(0).userId()))
            .willReturn(Optional.of(user));
        given(userRepository.findById(members.get(1).userId()))
            .willThrow(UserNotFound.class);

        assertThrows(UserNotFound.class, () -> {
            getAcceptedRegisterService.findAcceptedRegisters(targetGameId);
        });

        verify(registerRepository).findAllByGameId(targetGameId);
        verify(userRepository).findById(members.get(0).userId());
        verify(userRepository).findById(members.get(1).userId());
    }
}
