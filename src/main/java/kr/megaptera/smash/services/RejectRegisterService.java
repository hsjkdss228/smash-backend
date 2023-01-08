package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RejectRegisterService {
    private final RegisterRepository registerRepository;

    public RejectRegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public void rejectRegister(Long registerId) {
        Register register
            = registerRepository.findById(registerId)
            .orElseThrow(RegisterNotFound::new);

        register.reject();
    }
}
