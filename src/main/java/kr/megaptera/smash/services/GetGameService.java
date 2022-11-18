package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetGameService {
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    public GetGameService(GameRepository gameRepository,
                          MemberRepository memberRepository) {
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
    }

    public GameDetailDto findTargetGame(Long accessedUserId,
                                        Long targetPostId) {
        Game game = gameRepository.findByPostId(targetPostId)
            .orElseThrow(GameNotFound::new);

        List<Member> members = memberRepository.findAllByGameId(game.id());

        Boolean isRegistered = members.stream()
            .anyMatch(member -> member.userId().equals(accessedUserId));

        return new GameDetailDto(
            game.id(),
            game.exercise().name(),
            game.date().value(),
            game.place().name(),
            members.size(),
            game.targetMemberCount().value(),
            isRegistered
        );
    }
}
