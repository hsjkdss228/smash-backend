package kr.megaptera.smash.models.notice;

import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.models.notice.NoticeStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {
    @Test
    void active() {
        Notice noticeUnread = Notice.fake(NoticeStatus.unread());
        Notice noticeRead = Notice.fake(NoticeStatus.read());
        Notice noticeDeleted = Notice.fake(NoticeStatus.deleted());

        assertThat(noticeUnread.active()).isTrue();
        assertThat(noticeRead.active()).isTrue();
        assertThat(noticeDeleted.active()).isFalse();
    }

    @Test
    void unchecked() {
        Notice noticeUnread = Notice.fake(NoticeStatus.unread());

        assertThat(noticeUnread.unchecked()).isTrue();
    }

    @Test
    void read() {
        Notice notice = Notice.fake(NoticeStatus.unread());
        notice.read();

        assertThat(notice.status()).isEqualTo(NoticeStatus.read());
    }

    @Test
    void deleted() {
        Notice noticeUnread = Notice.fake(NoticeStatus.unread());
        noticeUnread.deleted();

        assertThat(noticeUnread.status()).isEqualTo(NoticeStatus.deleted());

        Notice noticeRead = Notice.fake(NoticeStatus.read());
        noticeRead.deleted();

        assertThat(noticeRead.status()).isEqualTo(NoticeStatus.deleted());
    }
}
