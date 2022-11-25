package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
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

        List<Register> members = registerRepository.findAllByGameId(game.id())
            .stream()
            .filter(register -> register.status().value()
                .equals(RegisterStatus.ACCEPTED))
            .toList();

        Register myRegister = registerRepository
            .findAllByGameIdAndUserId(game.id(), accessedUserId)
            .stream()
            .filter(register -> register.status().value()
                .equals(RegisterStatus.ACCEPTED)
                || register.status().value()
                .equals(RegisterStatus.PROCESSING))
            .findFirst().orElse(null);

        Long registerId = myRegister == null
            ? -1
            : myRegister.id();

        String registerStatus = myRegister == null
            ? "none"
            : switch (myRegister.status().value()) {
            case RegisterStatus.PROCESSING -> "processing";
            case RegisterStatus.ACCEPTED -> "accepted";
            default -> "none";
        };

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
