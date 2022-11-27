package kr.megaptera.smash.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// 이건 특별한 의미가 없습니다. Value Object인지 의심스럽습니다.
// 꼭 써야 한다면 “한계 값” 같은 식으로 행위 중심으로 뽑아보세요.
// 왜 한계 값이란 예를 들었냐면 limit.reach(count) 같은 “행위”와 연결되니까요.
@Embeddable
public class GameTargetMemberCount {
    @Column(name = "target_member_count")
    private Integer value;

    private GameTargetMemberCount() {

    }

    public GameTargetMemberCount(Integer value) {
        this.value = value;
    }

    // 뭔가 잘못 되고 있다는 강력한 신호입니다.
    public Integer value() {
        return value;
    }

    public boolean reach(long value) {
        return value >= this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        GameTargetMemberCount that = (GameTargetMemberCount) other;
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
