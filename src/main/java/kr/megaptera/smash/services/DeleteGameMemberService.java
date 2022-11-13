package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.MemberNotFound;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteGameMemberService {
    private final MemberRepository memberRepository;

    public DeleteGameMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // TODO: MemberNotFound Exception Handler 추가

    public void deleteGameMember(Long accessedUserId, Long gameId) {
        Member member
            = memberRepository.findByGameIdAndUserId(gameId, accessedUserId)
            .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }
}
