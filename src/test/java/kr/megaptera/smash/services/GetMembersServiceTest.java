package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.MemberDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetMembersServiceTest {
    private GetMembersService getMembersService;

    private MemberRepository memberRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        userRepository = mock(UserRepository.class);
        getMembersService
            = new GetMembersService(memberRepository,userRepository);
    }

    @Test
    void findTargetMembers() {
        Long targetGameId = 1L;
        List<Member> members = Member.fakes(2, targetGameId);
        List<User> users = User.fakes(2);

        given(memberRepository.findAllByGameId(targetGameId))
            .willReturn(members);
        given(userRepository.findById(members.get(0).userId()))
            .willReturn(Optional.of(users.get(0)));
        given(userRepository.findById(members.get(1).userId()))
            .willReturn(Optional.of(users.get(1)));

        MembersDetailDto membersDetailDto
            = getMembersService.findTargetMembers(targetGameId);

        assertThat(membersDetailDto).isNotNull();
        assertThat(membersDetailDto.getMembers().size()).isEqualTo(2);
    }
}
