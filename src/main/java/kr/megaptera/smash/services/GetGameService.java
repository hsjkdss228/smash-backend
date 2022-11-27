package kr.megaptera.smash.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;

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

        List<Register> members = registerRepository.findAllByGameId(game.id())
                .stream()
                .filter(Register::accepted)
                .toList();

        Register myRegister = registerRepository
                .findAllByGameIdAndUserId(game.id(), accessedUserId)
                .stream()
                .filter(Register::active)
                .findFirst().orElse(null);

        Long registerId = myRegister == null
                ? -1 // 뭔가 잘못 되고 있다는 강력한 신호입니다.
                : myRegister.id();

        String registerStatus = myRegister == null
                ? "none"
                : myRegister.toString();

        return new GameDetailDto(
                game.id(),
                game.exercise().name(),
                game.dateTime().joinDateAndTime(),
                game.place().name(),
                members.size(),
                game.targetMemberCount().value(),
                registerId,
                registerStatus
        );
    }
}
