package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.SignUpResultDto;
import kr.megaptera.smash.exceptions.AlreadyRegisteredUsername;
import kr.megaptera.smash.exceptions.UnmatchedPasswordAndConfirmPassword;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserAccount;
import kr.megaptera.smash.models.UserPersonalInformation;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpResultDto createUser(String name,
                                      String username,
                                      String password,
                                      String confirmPassword,
                                      String gender,
                                      String phoneNumber
    ) {
        Optional<User> foundUser
            = userRepository.findByAccountUsername(username);

        if (foundUser.isPresent()) {
            throw new AlreadyRegisteredUsername();
        }

        if (!password.equals(confirmPassword)) {
            throw new UnmatchedPasswordAndConfirmPassword();
        }

        User createdUser = new User(
            new UserAccount(username),
            new UserPersonalInformation(
                name,
                gender,
                phoneNumber
            )
        );

        createdUser.changePassword(password, passwordEncoder);

        User savedUser = userRepository.save(createdUser);

        return savedUser.toSignUpResultDto();
    }
}
