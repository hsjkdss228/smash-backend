package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.RegisterIdAndUserIdNotMatch;
import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatchRegisterToRejectedService {
    private final RegisterRepository registerRepository;

    public PatchRegisterToRejectedService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public void patchRegisterToRejected(Long registerId) {
        Register register
            = registerRepository.findById(registerId)
            .orElseThrow(RegisterNotFound::new);

        register.rejectRegister();
    }
}
