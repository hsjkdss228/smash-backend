package kr.megaptera.smash.models.notice;

import kr.megaptera.smash.models.notice.NoticeStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeStatusTest {
    @Test
    void equals() {
        assertThat(NoticeStatus.unread())
            .isEqualTo(NoticeStatus.unread());
        assertThat(NoticeStatus.read())
            .isEqualTo(NoticeStatus.read());
        assertThat(NoticeStatus.deleted())
            .isEqualTo(NoticeStatus.deleted());
    }
}
