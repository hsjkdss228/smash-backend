package kr.megaptera.smash.models.user;

import kr.megaptera.smash.models.user.UserMannerScore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMannerScoreTest {
    @Test
    void defaultScore() {
        assertThat(UserMannerScore.defaultScore())
            .isEqualTo(new UserMannerScore(5D));
    }
}
