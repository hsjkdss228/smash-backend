package kr.megaptera.smash.models;

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
    private UserName name;

    @Embedded
    private UserGender gender;

    private User() {

    }

    public User(Long id,
                UserName name,
                UserGender gender
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public Long id() {
        return id;
    }

    public UserName name() {
        return name;
    }

    public static User fake(String name) {
        return new User(
            1L,
            new UserName(name),
            new UserGender("여성")
        );
    }
}
