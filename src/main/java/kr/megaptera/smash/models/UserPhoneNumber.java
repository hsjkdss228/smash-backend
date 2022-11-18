package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPhoneNumber {
    @Column(name = "phone_number")
    private String value;

    private UserPhoneNumber() {

    }

    public UserPhoneNumber(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPhoneNumber that = (UserPhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserPhoneNumber{" +
            "value='" + value + '\'' +
            '}';
    }
}
