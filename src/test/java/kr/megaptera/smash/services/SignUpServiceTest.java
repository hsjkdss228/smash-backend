package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.SignUpResultDto;
import kr.megaptera.smash.exceptions.AlreadyRegisteredUsername;
import kr.megaptera.smash.exceptions.UnmatchedPasswordAndConfirmPassword;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserAccount;
import kr.megaptera.smash.models.UserPersonalInformation;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SignUpServiceTest {
    private SignUpService signUpService;

    private UserRepository userRepository;

    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String gender;
    private String phoneNumber;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        signUpService = new SignUpService(userRepository, passwordEncoder);

        name = "황인우";
        username = "hsjkdss228";
        password = "Password!1";
        confirmPassword = "Password!1";
        gender = "남성";
        phoneNumber = "01068772291";

        user = new User(
            1L,
            new UserAccount(username),
            new UserPersonalInformation(
                name,
                gender,
                phoneNumber
            )
        );
    }

    @Test
    void createUser() {
        given(userRepository.findByAccountUsername(username))
            .willReturn(Optional.empty());

        given(userRepository.save(any(User.class)))
            .willReturn(user);

        SignUpResultDto signUpResultDto = signUpService.createUser(
            name,
            username,
            password,
            confirmPassword,
            gender,
            phoneNumber
        );

        assertThat(signUpResultDto).isNotNull();
        assertThat(signUpResultDto.getEnrolledName()).isEqualTo(name);
    }

    @Test
    void alreadyRegisteredUsername() {
        given(userRepository.findByAccountUsername(username))
            .willReturn(Optional.of(user));

        assertThrows(AlreadyRegisteredUsername.class, () -> {
            signUpService.createUser(
                name,
                username,
                password,
                confirmPassword,
                gender,
                phoneNumber
            );
        });
    }

    @Test
    void unmatchedPasswordAndConfirmPassword() {
        String unmatchedPassword = "UnmatchedPassword!1";

        assertThrows(UnmatchedPasswordAndConfirmPassword.class, () -> {
            signUpService.createUser(
                name,
                username,
                password,
                unmatchedPassword,
                gender,
                phoneNumber
            );
        });
    }
}
