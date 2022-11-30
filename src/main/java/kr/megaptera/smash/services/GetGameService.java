package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetGameService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RegisterRepository registerRepository;

    public GetGameService(UserRepository userRepository,
                          GameRepository gameRepository,
                          RegisterRepository registerRepository
    ) {
        this.gameRepository = gameRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    public GameDetailDto findTargetGame(Long currentUserId,
                                        Long targetPostId) {
        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        Game game = gameRepository.findByPostId(targetPostId)
            .orElseThrow(GameNotFound::new);

        List<Register> registers = registerRepository.findAllByGameId(game.id());

        Integer currentMemberCount = game.countCurrentMembers(registers);

        Register myRegister = game.findMyRegister(currentUser, registers);

        Long registerId = myRegister == null
            ? null
            : myRegister.id();

        String registerStatus = myRegister == null
            ? "none"
            : myRegister.status().toString();

        return new GameDetailDto(
            game.id(),
            game.exercise().name(),
            game.dateTime().joinDateAndTime(),
            game.place().name(),
            currentMemberCount,
            game.targetMemberCount().value(),
            registerId,
            registerStatus
        );
    }
}
