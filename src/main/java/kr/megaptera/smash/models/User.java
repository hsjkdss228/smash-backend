package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.SignUpResultDto;
import kr.megaptera.smash.dtos.UserInPostDetailDto;
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

    @Embedded
    private UserProfileImage profileImage;

    @Embedded
    private UserMannerScore mannerScore;

    private User() {

    }

    // TODO: 기본으로 생성하도록 한 기본 프로필 이미지 Url은
    //   나중에 입력받는 Url을 이용할 수 있도록 수정

    public User(UserAccount account,
                UserPersonalInformation personalInformation) {
        this.account = account;
        this.personalInformation = personalInformation;
        this.profileImage = UserProfileImage.defaultImage();
        this.mannerScore = UserMannerScore.defaultScore();
    }

    public User(Long id,
                UserAccount account,
                UserPersonalInformation personalInformation
    ) {
        this.id = id;
        this.account = account;
        this.personalInformation = personalInformation;
        this.profileImage = UserProfileImage.defaultImage();
        this.mannerScore = UserMannerScore.defaultScore();
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

    public UserProfileImage profileImage() {
        return profileImage;
    }

    public UserMannerScore mannerScore() {
        return mannerScore;
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

    public UserInPostDetailDto toPostDetailDto() {
        return new UserInPostDetailDto(
            id,
            personalInformation.name(),
            personalInformation.gender(),
            personalInformation.phoneNumber(),
            profileImage.url(),
            mannerScore.value()
        );
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
        Long userId = 1L;
        return new User(
            userId,
            new UserAccount(account),
            new UserPersonalInformation(
                name, "여성", "01000000000"
            )
        );
    }

    public static List<User> fakes(long generationCount) {
        List<User> users = new ArrayList<>();
        for (long userId = 1; userId <= generationCount; userId += 1) {
            User user = new User(
                userId,
                new UserAccount("account" + userId),
                new UserPersonalInformation(
                    "사용자" + userId, "여성", "01000000000"
                )
            );
            users.add(user);
        }
        return users;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", account=" + account.toString() +
            ", personalInformation=" + personalInformation.toString() +
            '}';
    }
}
