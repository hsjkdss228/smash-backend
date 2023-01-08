package kr.megaptera.smash.models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserMannerScore {
    @Column(name = "manner_score")
    private Double value;

    private UserMannerScore() {

    }

    public UserMannerScore(Double value) {
        this.value = value;
    }

    public Double value() {
        return value;
    }

    public static UserMannerScore defaultScore() {
        return new UserMannerScore(5D);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMannerScore that = (UserMannerScore) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
