package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.AlreadyRegisteredGame;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
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
        // TODO: Controller에 GameNotFound Exception Handler,
        //   AlreadyRegisteredGame Exception Handler,
        //   UserNotFound Exception Handler 추가

        if (gameRepository.findById(gameId).isEmpty()) {
            throw new GameNotFound();
        }

        List<Member> members = memberRepository.findByGameId(gameId);

        members.forEach(member -> {
            if (member.userId().equals(accessedUserId)) {
                throw new AlreadyRegisteredGame();
            }
        });

        User user = userRepository.findById(accessedUserId)
            .orElseThrow(UserNotFound::new);

        Member member = new Member(
            accessedUserId,
            gameId,
            new MemberName(user.name().value())
        );
        Member savedMember = memberRepository.save(member);

        return new RegisterGameResultDto(savedMember.gameId());
    }
}
