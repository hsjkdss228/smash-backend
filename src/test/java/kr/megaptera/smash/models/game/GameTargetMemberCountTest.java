package kr.megaptera.smash.models.game;

import kr.megaptera.smash.models.game.GameTargetMemberCount;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTargetMemberCountTest {
    @Test
    void equals() {
        assertThat(new GameTargetMemberCount(10)).isEqualTo(new GameTargetMemberCount(10));
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(new GameTargetMemberCount(15));
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(null);
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(10);
    }

    @Test
    void notReached() {
        assertThat(new GameTargetMemberCount(10).reach(10)).isTrue();
    }

    @Test
    void reached() {
        assertThat(new GameTargetMemberCount(10).reach(8)).isFalse();
    }
}
