package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.MemberNotFound;
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

  public Member createMember(Long gameId,
                             Long teamId,
                             Long roleId,
                             Long userId) {
    Member member = new Member(gameId, teamId, roleId, userId);

    return memberRepository.save(member);
  }

  public void cancelRegister(Long roleId, Long accessedUserId) {
    List<Member> members = memberRepository.findAllByRoleId(roleId);

    for (Member member : members) {
      System.out.println("id of member: " + member.id());
      System.out.println("userId of member: " + member.userId());
    }

    Long targetMemberId = members.stream()
        .filter(member -> member.userId().equals(accessedUserId))
        .map(Member::id)
        .findFirst()
        .orElseThrow(MemberNotFound::new);

    System.out.println("targetMemberId: " + targetMemberId);

    memberRepository.deleteById(targetMemberId);
  }
}
