package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long verifyUser(String username, String password) {
        User user = userRepository.findByAccountUsername(username)
            .orElseThrow(() -> new LoginFailed("존재하지 않는 아이디입니다."));

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailed("비밀번호가 일치하지 않습니다.");
        }

        return user.id();
    }
}
