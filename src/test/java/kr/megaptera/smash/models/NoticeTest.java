package kr.megaptera.smash.models;

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
}
