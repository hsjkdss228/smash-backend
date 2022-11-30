package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterProcessingDto;
import kr.megaptera.smash.dtos.RegistersProcessingDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetProcessingRegisterService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public GetProcessingRegisterService(RegisterRepository registerRepository,
                                        UserRepository userRepository) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    public RegistersProcessingDto findApplicants(Long targetGameId) {
        List<Register> registers = registerRepository.findAllByGameId(targetGameId);

        List<RegisterProcessingDto> registerProcessingDtos
            = registers.stream()
            .filter(Register::processing)
            .map(register -> {
                Long userId = register.userId();

                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound(userId));

                return new RegisterProcessingDto(
                    register.id(),
                    user.name().value(),
                    user.gender().value(),
                    user.phoneNumber().value()
                );
            })
            .toList();

        return new RegistersProcessingDto(registerProcessingDtos);
    }
}
