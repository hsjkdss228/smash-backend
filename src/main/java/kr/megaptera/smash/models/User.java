package kr.megaptera.smash.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private UserAccount account;

    @Embedded
    private UserName name;

    @Embedded
    private UserGender gender;

    @Embedded
    private UserPhoneNumber phoneNumber;

    private User() {

    }

    public User(Long id,
                UserAccount account,
                UserName name,
                UserGender gender,
                UserPhoneNumber phoneNumber
    ) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public static List<User> fakes(long generationCount) {
        List<User> users = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            User user = new User(
                    id,
                    new UserAccount("UserIdentifier" + id),
                    new UserName("사용자 " + id),
                    new UserGender("성별"),
                    new UserPhoneNumber("010-0000-0000")
            );
            users.add(user);
        }
        return users;
    }

    public Long id() {
        return id;
    }

    public UserAccount account() {
        return account;
    }

    public UserName name() {
        return name;
    }

    public UserGender gender() {
        return gender;
    }

    public UserPhoneNumber phoneNumber() {
        return phoneNumber;
    }

    public static User fake(Long id) {
        return new User(
                id,
                new UserAccount("?????"),
                // 한극에서 ID라고 잘못 부르는 걸 표현하고 싶었다면
                // 올바른 표현은 username입니다.
                // https://www.google.com/search?q=account+username
                new UserName("이름"),
                new UserGender("여성"),
                new UserPhoneNumber("010-8888-8888")
        );
    }

    public static User fake(String name, String identifier) {
        return new User(
                1L,
                new UserAccount(identifier),
                new UserName(name),
                new UserGender("여성"),
                new UserPhoneNumber("010-8888-8888")
        );
    }
}
