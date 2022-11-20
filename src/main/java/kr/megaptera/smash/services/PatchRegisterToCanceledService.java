package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.RegisterIdAndUserIdNotMatch;
import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatchRegisterToCanceledService {
    private final RegisterRepository registerRepository;

    public PatchRegisterToCanceledService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public void patchRegisterToCanceled(Long registerId, Long accessedUserId) {
        Register register
            = registerRepository.findById(registerId)
            .orElseThrow(RegisterNotFound::new);

        if (!register.userId().equals(accessedUserId)) {
            throw new RegisterIdAndUserIdNotMatch();
        }

        register.cancelRegister();
    }
}
