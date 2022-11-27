package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTargetMemberCountTest {
    @Test
    void reach() {
        assertThat(new GameTargetMemberCount(10).reach(0)).isFalse();
        assertThat(new GameTargetMemberCount(10).reach(9)).isFalse();

        assertThat(new GameTargetMemberCount(10).reach(10)).isTrue();
        assertThat(new GameTargetMemberCount(10).reach(11)).isTrue();
    }

    @Test
    void equals() {
        assertThat(new GameTargetMemberCount(10)).isEqualTo(new GameTargetMemberCount(10));
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(new GameTargetMemberCount(15));
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(null);
        assertThat(new GameTargetMemberCount(10)).isNotEqualTo(10);
    }
}
