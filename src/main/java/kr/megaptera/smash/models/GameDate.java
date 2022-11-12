package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GameDate {
    @Column(name = "date")
    private String value;

    private GameDate() {

    }

    public GameDate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDate gameDate = (GameDate) o;
        return Objects.equals(value, gameDate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "GameDate{" +
            "value='" + value + '\'' +
            '}';
    }
}
