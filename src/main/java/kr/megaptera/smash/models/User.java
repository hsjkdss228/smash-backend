package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.SignUpResultDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private UserAccount account;

    @Column(name = "password")
    private String encodedPassword;

    @Embedded
    private UserPersonalInformation personalInformation;

    private User() {

    }

    public User(UserAccount account,
                UserPersonalInformation personalInformation) {
        this.account = account;
        this.personalInformation = personalInformation;
    }

    public User(Long id,
                UserAccount account,
                UserPersonalInformation personalInformation
    ) {
        this.id = id;
        this.account = account;
        this.personalInformation = personalInformation;
    }

    public Long id() {
        return id;
    }

    public UserAccount account() {
        return account;
    }

    public UserPersonalInformation personalInformation() {
        return personalInformation;
    }

    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        encodedPassword = passwordEncoder.encode(password);
    }

    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public SignUpResultDto toSignUpResultDto() {
        return new SignUpResultDto(personalInformation.name());
    }

    public static User fake(Long userId) {
        return new User(
            userId,
            new UserAccount("username1"),
            new UserPersonalInformation(
                "사용자명", "여성", "01000000000"
            )
        );
    }

    public static User fake(String name, String account) {
        return new User(
            1L,
            new UserAccount(account),
            new UserPersonalInformation(
                name, "여성", "01000000000"
            )
        );
    }

    public static List<User> fakes(long generationCount) {
        List<User> users = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            User user = new User(
                id,
                new UserAccount("account" + id),
                new UserPersonalInformation(
                    "사용자" + id, "여성", "01000000000"
                )
            );
            users.add(user);
        }
        return users;
    }
}
