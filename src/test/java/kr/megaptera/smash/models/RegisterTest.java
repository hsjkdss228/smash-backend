package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterTest {
    @Test
    void match() {
        Long userId = 1L;
        Long gameId = 1L;

        User user = User.fake(userId);
        assertThat(Register.fake(userId, gameId).match(user))
            .isTrue();
        Long wrongUserId = 9999L;
        assertThat(Register.fake(wrongUserId, gameId).match(user))
            .isFalse();
    }

    @Test
    void active() {
        Long userId = 1L;
        Long gameId = 1L;
        assertThat(Register.fakeProcessing(userId, gameId).active()).isTrue();
        assertThat(Register.fakeAccepted(userId, gameId).active()).isTrue();
        assertThat(Register.fakeCanceled(userId, gameId).active()).isFalse();
        assertThat(Register.fakeRejected(userId, gameId).active()).isFalse();
    }

    @Test
    void accepted() {
        Long userId = 1L;
        Long gameId = 1L;
        assertThat(Register.fakeProcessing(userId, gameId).accepted()).isFalse();
        assertThat(Register.fakeAccepted(userId, gameId).accepted()).isTrue();
        assertThat(Register.fakeCanceled(userId, gameId).accepted()).isFalse();
        assertThat(Register.fakeRejected(userId, gameId).accepted()).isFalse();
    }

    @Test
    void processing() {
        Long userId = 1L;
        Long gameId = 1L;
        assertThat(Register.fakeProcessing(userId, gameId).processing()).isTrue();
        assertThat(Register.fakeAccepted(userId, gameId).processing()).isFalse();
        assertThat(Register.fakeCanceled(userId, gameId).processing()).isFalse();
        assertThat(Register.fakeRejected(userId, gameId).processing()).isFalse();
    }

    @Test
    void cancelRegister() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.cancelRegister();
        assertThat(register.status()).isEqualTo(RegisterStatus.canceled());
    }

    @Test
    void acceptRegister() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.acceptRegister();
        assertThat(register.status()).isEqualTo(RegisterStatus.accepted());
    }

    @Test
    void rejectRegister() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.rejectRegister();
        assertThat(register.status()).isEqualTo(RegisterStatus.rejected());
    }
}
