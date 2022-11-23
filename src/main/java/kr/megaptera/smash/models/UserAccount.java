package kr.megaptera.smash.models;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserAccount {
    @Column(name = "identifier")
    private String identifier;

    @Column(name = "password")
    private String encodedPassword;

    public UserAccount() {

    }

    public UserAccount(String identifier) {
        this.identifier = identifier;
    }

    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        encodedPassword = passwordEncoder.encode(password);
    }

    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
            "identifier='" + identifier + '\'' +
            '}';
    }
}
