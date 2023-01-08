package kr.megaptera.smash.models.register;

import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.game.GameDateTime;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.models.notice.NoticeStatus;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.models.register.RegisterStatus;
import kr.megaptera.smash.models.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterTest {
    @Test
    void matchWithUser() {
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
    void matchWithUserId() {
        Long userId = 1L;
        Long gameId = 1L;

        assertThat(Register.fake(userId, gameId).match(userId))
            .isTrue();
        Long wrongUserId = 9999L;
        assertThat(Register.fake(userId, gameId).match(wrongUserId))
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
    void cancel() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.cancel();
        assertThat(register.status()).isEqualTo(RegisterStatus.canceled());
    }

    @Test
    void accept() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.accept();
        assertThat(register.status()).isEqualTo(RegisterStatus.accepted());
    }

    @Test
    void reject() {
        Long userId = 1L;
        Long gameId = 1L;
        Register register
            = Register.fake(userId, gameId, RegisterStatus.processing());
        register.reject();
        assertThat(register.status()).isEqualTo(RegisterStatus.rejected());
    }

    @Test
    void createRegisterNotice() {
        User user = User.fake("신청자 1", "username");
        Long postAuthorId = 2L;
        User postAuthor = User.fake(postAuthorId);

        Long gameId = 1L;
        Register register = Register.fake(user.id(), gameId);

        Notice notice = register.createRegisterNotice(user, postAuthor);
        assertThat(notice.userId()).isEqualTo(postAuthorId);
        assertThat(notice.contents().title()).contains("신청이 등록되었습니다.");
        assertThat(notice.contents().detail()).isEqualTo("등록한 신청자: 신청자 1");
        assertThat(notice.status()).isEqualTo(NoticeStatus.unread());
    }

    @Test
    void createAcceptNotice() {
        Long userId = 1L;
        User user = User.fake(userId);

        Long gameId = 1L;
        Game game = Game.fake(
            "운동 이름",
            new GameDateTime(
                LocalDate.of(2022, 12, 25),
                LocalTime.of(12, 30),
                LocalTime.of(15, 0)
            )
        );
        Register register = Register.fakeAccepted(game.id(), user.id());

        Notice notice = register.createAcceptNotice(user, game);
        assertThat(notice.userId()).isEqualTo(user.id());
        assertThat(notice.contents().title()).contains("참가가 확정되었습니다.");
        assertThat(notice.contents().detail())
            .contains("운동 이름");
        assertThat(notice.contents().detail())
            .contains("2022년 12월 25일 오후 12:30 ~ 오후 03:00");
        assertThat(notice.status()).isEqualTo(NoticeStatus.unread());
    }
}
