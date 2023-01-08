package kr.megaptera.smash.models.game;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GameTargetMemberCount {
    @Column(name = "target_member_count")
    private Integer value;

    private GameTargetMemberCount() {

    }

    public GameTargetMemberCount(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public boolean reach(long count) {
        return count >= value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameTargetMemberCount that = (GameTargetMemberCount) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
