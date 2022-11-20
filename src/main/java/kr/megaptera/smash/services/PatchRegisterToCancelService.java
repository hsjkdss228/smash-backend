package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatchRegisterToCancelService {
    private final RegisterRepository registerRepository;

    public PatchRegisterToCancelService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    // TODO: RegisterNotFound Exception Handler 추가

    public void patchRegisterToCancel(Long accessedUserId, Long gameId) {
        Register register
            = registerRepository.findByGameIdAndUserId(gameId, accessedUserId)
            .orElseThrow(RegisterNotFound::new);

        register.cancelRegister();
    }
}
