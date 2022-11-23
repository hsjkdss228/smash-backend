package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.ApplicantDetailDto;
import kr.megaptera.smash.dtos.ApplicantsDetailDto;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserAccount;
import kr.megaptera.smash.models.UserGender;
import kr.megaptera.smash.models.UserName;
import kr.megaptera.smash.models.UserPhoneNumber;
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

        applicants = List.of(
            new Register(
                1L,
                1L,
                1L,
                new RegisterStatus(RegisterStatus.PROCESSING)
            ),
            new Register(
                2L,
                2L,
                1L,
                new RegisterStatus(RegisterStatus.PROCESSING)
            ),
            new Register(
                3L,
                3L,
                1L,
                new RegisterStatus(RegisterStatus.ACCEPTED)
            ),
            new Register(
                4L,
                4L,
                1L,
                new RegisterStatus(RegisterStatus.CANCELED)
            )
        );
        users = List.of(
            new User(
                1L,
                new UserAccount("hsjkdss228"),
                new UserName("사용자 이름 1"),
                new UserGender("남성"),
                new UserPhoneNumber("010-1234-5678")
            ),
            new User(
                2L,
                new UserAccount("dhkddlsgn228"),
                new UserName("사용자 이름 2"),
                new UserGender("여성"),
                new UserPhoneNumber("010-2345-6789")
            )
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

        ApplicantsDetailDto applicantsDetailDto
            = getProcessingRegisterService.findApplicants(targetGameId);

        assertThat(applicantsDetailDto).isNotNull();
        List<ApplicantDetailDto> applicantDetailDtos
            = applicantsDetailDto.getApplicants();
        assertThat(applicantDetailDtos.size()).isEqualTo(2);
        assertThat(applicantDetailDtos.get(0).getName()).isEqualTo("사용자 이름 1");
        assertThat(applicantDetailDtos.get(1).getGender()).isEqualTo("여성");

        verify(registerRepository).findAllByGameId(targetGameId);
        verify(userRepository).findById(applicants.get(0).userId());
        verify(userRepository).findById(applicants.get(1).userId());
    }
}
