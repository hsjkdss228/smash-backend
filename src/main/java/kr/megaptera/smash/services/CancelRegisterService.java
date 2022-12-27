package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.IsNotRegisterOfCurrentUser;
import kr.megaptera.smash.exceptions.RegisterNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CancelRegisterService {
    private final RegisterRepository registerRepository;

    public CancelRegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public void cancelRegister(Long registerId, Long accessedUserId) {
        Register register
            = registerRepository.findById(registerId)
            .orElseThrow(RegisterNotFound::new);

        if (!register.match(accessedUserId)) {
            throw new IsNotRegisterOfCurrentUser();
        }

        register.cancel();
    }
}
