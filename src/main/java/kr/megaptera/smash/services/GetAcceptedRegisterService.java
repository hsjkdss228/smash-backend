package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterAcceptedDto;
import kr.megaptera.smash.dtos.RegistersAcceptedDto;
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
public class GetAcceptedRegisterService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public GetAcceptedRegisterService(RegisterRepository registerRepository,
                                      UserRepository userRepository) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    public RegistersAcceptedDto findAcceptedRegisters(Long targetGameId) {
        List<Register> registers = registerRepository.findAllByGameId(targetGameId);

        List<RegisterAcceptedDto> registerAcceptedDtos
            = registers.stream()
            .filter(Register::accepted)
            .map(register -> {
                Long userId = register.userId();

                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound(userId));

                return new RegisterAcceptedDto(
                    register.id(),
                    user.toPostDetailDto()
                );
            })
            .toList();

        return new RegistersAcceptedDto(registerAcceptedDtos);
    }
}
