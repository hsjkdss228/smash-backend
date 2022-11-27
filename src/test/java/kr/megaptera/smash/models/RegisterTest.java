package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterTest {
    static final RegisterStatus PROCESSING = RegisterStatus.processing();
    static final RegisterStatus CANCELED = RegisterStatus.canceled();
    static final RegisterStatus ACCEPTED = RegisterStatus.accepted();
    static final RegisterStatus REJECTED = RegisterStatus.rejected();

    @Test
    void matchUser() {
        User user = User.fake(1L);
        Long gameId = 1L;

        assertThat(Register.fake(1L, gameId).match(user)).isTrue();

        assertThat(Register.fake(2L, gameId).match(user)).isFalse();
    }

    @Test
    void active() {
        assertThat(Register.fake(PROCESSING).active()).isTrue();
        assertThat(Register.fake(ACCEPTED).active()).isTrue();

        assertThat(Register.fake(CANCELED).active()).isFalse();
        assertThat(Register.fake(REJECTED).active()).isFalse();
    }

    @Test
    void accepted() {
        assertThat(Register.fake(ACCEPTED).accepted()).isTrue();

        assertThat(Register.fake(PROCESSING).accepted()).isFalse();
        assertThat(Register.fake(CANCELED).accepted()).isFalse();
        assertThat(Register.fake(REJECTED).accepted()).isFalse();
    }

    // 테스트 코드가 아주 많이 필요합니다.
    // 도메인 모델을 만들 때 TDD를 안 했다는 강력한 증거죠.
}
