package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterProcessingDto;
import kr.megaptera.smash.dtos.RegistersProcessingDto;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetProcessingRegisterServiceTest {
    private GetProcessingRegisterService getProcessingRegisterService;

    private RegisterRepository registerRepository;
    private UserRepository userRepository;

    List<Register> applicants;
    List<User> users;

    @BeforeEach
    void setUp() {
        registerRepository = mock(RegisterRepository.class);
        userRepository = mock(UserRepository.class);
        getProcessingRegisterService
            = new GetProcessingRegisterService(
            registerRepository, userRepository);

        Long gameId = 1L;
        applicants = List.of(
            Register.fake(1L, gameId, RegisterStatus.processing()),
            Register.fake(2L, gameId, RegisterStatus.processing()),
            Register.fake(3L, gameId, RegisterStatus.accepted()),
            Register.fake(4L, gameId, RegisterStatus.canceled())
        );
        users = List.of(
            User.fake("사용자 이름 1", "hsjkdss228"),
            User.fake("사용자 이름 2", "dhkddlsgn228")
        );
    }

    @Test
    void findApplicants() {
        Long targetGameId = 1L;
        given(registerRepository.findAllByGameId(targetGameId))
            .willReturn(applicants);
        given(userRepository.findById(applicants.get(0).userId()))
            .willReturn(Optional.of(users.get(0)));
        given(userRepository.findById(applicants.get(1).userId()))
            .willReturn(Optional.of(users.get(1)));

        RegistersProcessingDto registersProcessingDto
            = getProcessingRegisterService.findApplicants(targetGameId);

        assertThat(registersProcessingDto).isNotNull();
        List<RegisterProcessingDto> registerProcessingDtos
            = registersProcessingDto.getApplicants();
        assertThat(registerProcessingDtos.size()).isEqualTo(2);
        assertThat(registerProcessingDtos.get(0)
            .getUserInformation().getName()).isEqualTo("사용자 이름 1");
        assertThat(registerProcessingDtos.get(1)
            .getUserInformation().getName()).isEqualTo("사용자 이름 2");

        verify(registerRepository).findAllByGameId(targetGameId);
        verify(userRepository).findById(applicants.get(0).userId());
        verify(userRepository).findById(applicants.get(1).userId());
    }
}
