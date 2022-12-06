package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetUserNameService {
    private final UserRepository userRepository;

    public GetUserNameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserNameDto getUserName(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        String name = user.personalInformation().name();

        return new UserNameDto(name);
    }
}
