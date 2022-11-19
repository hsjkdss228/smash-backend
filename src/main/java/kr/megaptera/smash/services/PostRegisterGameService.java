package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostRegisterGameService {
    private final GameRepository gameRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public PostRegisterGameService(GameRepository gameRepository,
                                   RegisterRepository registerRepository,
                                   UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    public RegisterGameResultDto registerGame(Long gameId,
                                              Long accessedUserId) {
        if (gameRepository.findById(gameId).isEmpty()) {
            throw new RegisterGameFailed(
                "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다.");
        }

        List<Register> applicantsAndMembers = registerRepository.findByGameId(gameId);

        applicantsAndMembers.forEach(person -> {
            if (person.userId().equals(accessedUserId)
                && (person.status().value().equals(RegisterStatus.PROCESSING)
                || person.status().value().equals(RegisterStatus.ACCEPTED))) {
                throw new RegisterGameFailed("이미 신청 중이거나 신청이 완료된 운동입니다.");
            }
        });

        User user = userRepository.findById(accessedUserId)
            .orElseThrow(() -> new RegisterGameFailed(
                "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다."));

        Register register = new Register(
            accessedUserId,
            gameId,
            new RegisterStatus(RegisterStatus.PROCESSING)
        );
        Register saved = registerRepository.save(register);

        return new RegisterGameResultDto(saved.gameId());
    }
}
