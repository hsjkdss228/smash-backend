package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User verifyUser(Long userId) {
        try {
            return userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
        } catch (UserNotFound exception) {
            throw new LoginFailed("존재하지 않는 User Id 입니다. (201)");
        }
    }
}
