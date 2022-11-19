package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetGameService {
    private final GameRepository gameRepository;
    private final RegisterRepository registerRepository;

    public GetGameService(GameRepository gameRepository,
                          RegisterRepository registerRepository) {
        this.gameRepository = gameRepository;
        this.registerRepository = registerRepository;
    }

    public GameDetailDto findTargetGame(Long accessedUserId,
                                        Long targetPostId) {
        Game game = gameRepository.findByPostId(targetPostId)
            .orElseThrow(GameNotFound::new);

        List<Register> members = registerRepository.findAllByGameId(game.id());

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
