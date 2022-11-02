package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
  private final MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public List<Member> members() {
    return memberRepository.findAll();
  }

  public Integer membersCountByTeam(Long teamId) {
    List<Member> membersWithTeam = memberRepository.findAllByTeamId(teamId);

    return membersWithTeam.size();
  }

  public Integer membersCountByRole(Long roleId) {
    List<Member> membersWithRole = memberRepository.findAllByRoleId(roleId);

    return membersWithRole.size();
  }
}
