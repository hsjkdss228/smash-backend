package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MemberServiceTest {
  private MemberService memberService;
  private MemberRepository memberRepository;

  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    memberService = new MemberService(memberRepository);
  }

  @Test
  void members() {
    List<Member> members = List.of(
        new Member(1L, 1L, 2L, 1L),
        new Member(2L, 1L, 2L, 2L),
        new Member(3L, 1L, 3L, 3L),
        new Member(4L, 1L, 3L, 4L),
        new Member(5L, 2L, 4L, 5L)
    );

    given(memberRepository.findAll()).willReturn(members);

    List<Member> foundMembers = memberService.members();

    assertThat(foundMembers).isNotNull();
    assertThat(foundMembers).hasSize(5);

    verify(memberRepository).findAll();
  }

  @Test
  void membersCountByTeam() {
    List<Member> membersWithTeam = List.of(
        new Member(1L, 1L, 2L, 1L),
        new Member(2L, 1L, 2L, 2L),
        new Member(3L, 1L, 3L, 3L),
        new Member(4L, 1L, 3L, 4L)
    );
    Long teamId = 1L;
    given(memberRepository.findAllByTeamId(teamId)).willReturn(membersWithTeam);

    Integer count = memberService.membersCountByTeam(teamId);

    assertThat(count).isEqualTo(4);

    verify(memberRepository).findAllByTeamId(any(Long.class));
  }

  @Test
  void membersCountByRole() {
    List<Member> membersWithRoleId1L = List.of();
    Long roleId1 = 1L;

    given(memberRepository.findAllByRoleId(roleId1))
        .willReturn(membersWithRoleId1L);
    Integer count1 = memberService.membersCountByRole(roleId1);
    assertThat(count1).isEqualTo(0);

    verify(memberRepository).findAllByRoleId(1L);

    List<Member> membersWithRoleId2L = List.of(
        new Member(1L, 1L, 2L, 1L),
        new Member(2L, 1L, 2L, 2L)
    );
    Long roleId2 = 2L;
    given(memberRepository.findAllByRoleId(roleId2))
        .willReturn(membersWithRoleId2L);
    Integer count2 = memberService.membersCountByRole(roleId2);
    assertThat(count2).isEqualTo(2);

    verify(memberRepository).findAllByRoleId(2L);
  }
}
