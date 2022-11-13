package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.MemberName;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostRegisterGameService {
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public PostRegisterGameService(GameRepository gameRepository,
                                   MemberRepository memberRepository,
                                   UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    public RegisterGameResultDto registerGame(Long gameId,
                                              Long accessedUserId) {
        if (gameRepository.findById(gameId).isEmpty()) {
            throw new RegisterGameFailed(
                "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다.");
        }

        List<Member> members = memberRepository.findByGameId(gameId);

        members.forEach(member -> {
            if (member.userId().equals(accessedUserId)) {
                throw new RegisterGameFailed("이미 신청이 완료된 운동입니다.");
            }
        });

        User user = userRepository.findById(accessedUserId)
            .orElseThrow(() -> new RegisterGameFailed(
                "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다."));

        Member member = new Member(
            accessedUserId,
            gameId,
            new MemberName(user.name().value())
        );
        Member savedMember = memberRepository.save(member);

        return new RegisterGameResultDto(savedMember.gameId());
    }
}
