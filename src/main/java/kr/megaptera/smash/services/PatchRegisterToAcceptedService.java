package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.NoticeRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatchRegisterToAcceptedService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final NoticeRepository noticeRepository;

    public PatchRegisterToAcceptedService(RegisterRepository registerRepository,
                                          UserRepository userRepository,
                                          GameRepository gameRepository,
                                          NoticeRepository noticeRepository) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.noticeRepository = noticeRepository;
    }

    public void patchRegisterToAccepted(Long registerId) {
        Register register
            = registerRepository.findById(registerId)
            .orElseThrow(RegisterNotFound::new);

        register.acceptRegister();

        if (register.accepted()) {
            User user = userRepository.findById(register.userId())
                .orElseThrow(() -> new UserNotFound(register.userId()));

            Game game = gameRepository.findById(register.gameId())
                .orElseThrow(GameNotFound::new);

            Notice notice = register.createAcceptNotice(user, game);

            noticeRepository.save(notice);
        }
    }
}
