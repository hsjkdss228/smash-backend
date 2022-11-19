package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.models.Register;
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
    void findTargetMembers() {
        Long targetGameId = 1L;
        List<Register> members = Register.fakeMembers(2, targetGameId);
        List<User> users = User.fakes(2);

        given(registerRepository.findAllByGameId(targetGameId))
            .willReturn(members);
        given(userRepository.findById(members.get(0).userId()))
            .willReturn(Optional.of(users.get(0)));
        given(userRepository.findById(members.get(1).userId()))
            .willReturn(Optional.of(users.get(1)));

        MembersDetailDto membersDetailDto
            = getAcceptedRegisterService.findMembers(targetGameId);

        assertThat(membersDetailDto).isNotNull();
        assertThat(membersDetailDto.getMembers().size()).isEqualTo(2);
    }
}
