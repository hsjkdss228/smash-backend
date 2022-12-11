package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMannerScoreTest {
    @Test
    void defaultScore() {
        assertThat(UserMannerScore.defaultScore())
            .isEqualTo(new UserMannerScore(5D));
    }
}
